package se.kth.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.kth.project.dto.BookingDTO;
import se.kth.project.dto.CourseDTO;
import se.kth.project.dto.ListDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.*;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.CourseService;
import se.kth.project.service.ReservationService;
import se.kth.project.service.UserService;

import java.util.List;
import java.util.Objects;

/**
 * Controller handling reservation-related requests.
 * <p>
 * This controller provides methods for creating and managing reservations, displaying reservation lists,
 * and handling reservation-related actions for both administrators and students.
 *
 * @see ReservationService
 * @see CourseService
 * @see UserService
 * @see ListDTO
 * @see CourseDTO
 * @see UserDTO
 * @see BookingDTO
 */
@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final CourseService courseService;
    private final UserService userService;

    /**
     * Constructs a new instance of the {@code ReservationController} class.
     *
     * @param reservationService The service responsible for reservation-related operations.
     * @param courseService      The service responsible for course-related operations.
     * @param userService        The service responsible for user-related operations.
     */
    @Autowired
    public ReservationController(ReservationService reservationService,
                                 CourseService courseService,
                                 UserService userService) {
        this.reservationService = reservationService;
        this.courseService = courseService;
        this.userService = userService;
    }

    /**
     * Displays the reservation list creation form for admins.
     * <p>
     * This method allows administrators to create new reservation lists by providing necessary details,
     * such as the course for the list.
     *
     * @param model The Spring MVC model for rendering the view.
     * @return The view name for the reservation list creation form.
     */
    @GetMapping("/create-list")
    public String displayCreateReservationListForm(Model model) {
        if (SecurityUtil.isUserAdmin()) {
            List<CourseDTO> courses = courseService.getAllCourses();
            ListDTO list = new ListDTO();
            model.addAttribute("courses", courses);
            model.addAttribute("list", list);
            return "create-list";
        }
        return "redirect:/home?unauthorized";
    }

    @PostMapping("/create-list/save")
    public String createNewReservationList(@Valid @ModelAttribute("list") ListDTO list,
                                           BindingResult result,
                                           Model model) {
        if (result.hasErrors()) {
            List<CourseDTO> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("list", list);
            return "create-list";
        }
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            UserEntity user = userService.findByUsername(username);
            CourseEntity course = courseService.findById(list.getCourseId());
            list.setUser(user);
            list.setCourse(course);
            reservationService.saveList(list);
            return "redirect:/reservation-list?success=Reservation created successfully !";
        }
        return "redirect:/create-list?error";
    }

    /**
     * Displays the reservation list page.
     * <p>
     * This method allows both administrators and students to view reservation lists.
     * For administrators, it shows all reservation lists. For students, it shows only the lists they have access to.
     *
     * @param model The Spring MVC model for rendering the view.
     * @return The view name for the reservation list page.
     */
    @GetMapping("/reservation-list")
    public String displayReservations(Model model) {
        UserDTO user = userService.convertToDTO(userService.findByUsername(SecurityUtil.getSessionUser()));
        boolean NO_LISTS = false;
        model.addAttribute("NO_LISTS", NO_LISTS);
        if (SecurityUtil.isUserAdmin()) {
            List<ListEntity> reservationLists = reservationService.getAllLists();
            model.addAttribute("reservationLists", reservationLists);
            int[] spots = new int[reservationLists.size()];
            for (int i = 0; i < reservationLists.size(); i++) {
                spots[i] = reservationService.getNumberOfAvailableSpotsLeft(reservationLists.get(i).getId());
            }
            model.addAttribute("spots", spots);
            return "reservation-list";
        } else {
            List<CourseEntity> courses = user.getCourses();
            List<ListEntity> reservationLists = reservationService.getAllListsThatStudentHasAccessTo(courses);
            if (reservationLists == null) {
                NO_LISTS = true;
                model.addAttribute("NO_LISTS", NO_LISTS);
            } else {
                model.addAttribute("reservationLists", reservationLists);
                int[] spots = new int[reservationLists.size()];
                for (int i = 0; i < reservationLists.size(); i++) {
                    spots[i] = reservationService.getNumberOfAvailableSpotsLeft(reservationLists.get(i).getId());
                }
                model.addAttribute("spots", spots);
            }
            return "reservation-list";
        }
    }

    /**
     * Deletes a reservation list.
     * <p>
     * This method allows administrators to delete a reservation list by providing the list's ID.
     *
     * @param listId The ID of the reservation list to be deleted.
     * @return The redirect path after deleting the reservation list, or redirect to home without deleting if user role is not admin.
     */
    @GetMapping("/reservation-list/{listId}/delete")
    public String deleteReservation(@PathVariable int listId) {
        if (SecurityUtil.isUserAdmin()) {
            reservationService.deleteReservationList(listId);
            System.out.println("BookingDTO removed");
            return "redirect:/reservation-list?success=Reservation deleted successfully !";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    /**
     * Displays the reservation booking form.
     * <p>
     * This method allows both administrators and students to book slots on a reservation list.
     *
     * @param listId The ID of the reservation list for which the booking is made.
     * @param model  The Spring MVC model for rendering the view.
     * @return The view name for the reservation booking form.
     */
    @GetMapping("/reservation-list/{listId}/book")
    public String bookReservationForm(@PathVariable("listId") Integer listId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (SecurityUtil.getSessionUser() == null) {
            throw new UsernameNotFoundException("User could not be found");
        }
        // get list to display course info
        ListDTO list = reservationService.findListById(listId);
        model.addAttribute("list", list);
        //send in the booking object so that we can list times and slots that are bookable
        BookingDTO booking = reservationService.createBookingObject(listId);
        model.addAttribute("booking", booking);
        if (SecurityUtil.isUserAdmin()) { //FOR ADMINS
            //get all students that have course access to the course with the corresponding course id
            List<UserDTO> students = userService.retrieveAllStudentsForCourseByListId(listId);
            model.addAttribute("students", students);
            return "book";
        } else if (!SecurityUtil.isUserAdmin()) { //FOR STUDENTS
            UserDTO user = userService.convertToDTO(userService.findByUsername(username));
            model.addAttribute("user", user);
            booking.setStudentId(user.getUserId());
            List<UserDTO> students = userService.retrieveAllStudentsForCourseByListId(listId);
            students.remove(user);
            model.addAttribute("students", students);
            return "book";
        }
        return "redirect:/reservation-list";
    }

    /**
     * Saves a student's reservation.
     * <p>
     * This method is called when an admin/student submits a reservation booking form.
     *
     * @param booking The booking information submitted by the student.
     * @return The redirect path after saving the reservation.
     */
    @PostMapping("/book/save")
    public String saveStudentReservation(@ModelAttribute("booking") BookingDTO booking) {
        System.out.println(booking);
        reservationService.createReservation(booking);
        return "redirect:/reservation-list?success=Booking created !";
    }

    /**
     * Displays the booking management page for a specific student.
     * <p>
     * This method allows both administrators and students to view and manage bookings. Admin can manage all students
     * bookings, while a student can only manage their own booking.
     *
     * @param studentId The ID of the student for whom the bookings are managed.
     * @param model     The Spring MVC model for rendering the view.
     * @return The view name for the booking management page.
     */
    @GetMapping("manage-students/{studentId}/manage-bookings")
    public String manageBookings(@PathVariable("studentId") Integer studentId, Model model) {
        if (SecurityUtil.isUserAdmin() || Objects.equals(userService.findByUsername(SecurityUtil.getSessionUser()).getId(), studentId)) {
            List<ReservationEntity> reservations = reservationService.getAllReservationsForStudent(studentId);
            model.addAttribute("reservations", reservations);
            UserDTO user = userService.convertToDTO(userService.findById(studentId));
            model.addAttribute("user", user);
            return "manage-bookings";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    /**
     * Deletes a reservation.
     * <p>
     * This method allows administrators to delete a reservation, or a student to delete their own reservation.
     *
     * @param reservationId The ID of the reservation to be deleted.
     * @return The redirect path after deleting the reservation.
     */
    @GetMapping("/manage-bookings/{reservationId}/delete/{studentId}")
    public String deleteReservation(@PathVariable("reservationId") Integer reservationId) {
        ReservationEntity reservation = reservationService.findByReservationId(reservationId);
        if (SecurityUtil.isUserAdmin() ||
                userService.findByUsername(SecurityUtil.getSessionUser()).getId().equals(reservation.getUser().getId())) {
            reservationService.deleteReservation(reservationId);
            return "redirect:/manage-students/{studentId}/manage-bookings?success";
        }
        return "redirect:/home?unauthorized";
    }
}

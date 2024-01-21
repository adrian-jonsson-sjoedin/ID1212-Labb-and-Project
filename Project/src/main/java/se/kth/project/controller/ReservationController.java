package se.kth.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public ReservationController(ReservationService reservationService,
                                 CourseService courseService,
                                 UserService userService) {
        this.reservationService = reservationService;
        this.courseService = courseService;
        this.userService = userService;
    }


    @GetMapping("/create-list")
    public String createReservationList(Model model) {
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
            return "redirect:/reservation-list?success";
        }
        return "redirect:/create-list?error";
    }

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

    @GetMapping("/reservation-list/{listId}/delete")
    public String deleteReservation(@PathVariable int listId, Model model) {
        if (SecurityUtil.isUserAdmin()) {
            reservationService.deleteReservationList(listId);
            System.out.println("Booking removed");
            return "redirect:/reservation-list";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

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
        Booking booking = reservationService.createBookingObject(listId);
        model.addAttribute("booking", booking);
        if (SecurityUtil.isUserAdmin()) { //FOR ADMINS
            //get all students that have course access to the course with the corresponding course id
            List<UserDTO> students = userService.retrieveAllStudentsForCourseByListId(listId);
            model.addAttribute("students", students);
            return "book";
        } else if (!SecurityUtil.isUserAdmin()) {
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

    @PostMapping("/book/save")
    public String saveStudentReservation(@ModelAttribute("booking") Booking booking) {
        System.out.println(booking);
        reservationService.createReservation(booking);
        return "redirect:/reservation-list?success";
    }

    @GetMapping("manage-students/{studentId}/manage-bookings")
    public String manageBookings(@PathVariable("studentId") Integer studentId, Model model) {
        if (SecurityUtil.isUserAdmin() || Objects.equals(userService.findByUsername(SecurityUtil.getSessionUser()).getId(), studentId)) {
            List<ReservationEntity> reservations = reservationService.getAllReservationsForStudent(studentId, studentId);
            model.addAttribute("reservations", reservations);
            UserDTO user = userService.convertToDTO(userService.findById(studentId));
            model.addAttribute("user", user);
            return "manage-bookings";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    @GetMapping("/manage-bookings/{reservationId}/delete/{studentId}")
    public String deleteReservation(@PathVariable("reservationId") Integer reservationId,
                                    @PathVariable("studentId") Integer studentId) {
        ReservationEntity reservation = reservationService.findByReservationId(reservationId);
        if (SecurityUtil.isUserAdmin() ||
                userService.findByUsername(SecurityUtil.getSessionUser()).getId().equals(reservation.getUser().getId())) {
            reservationService.deleteReservation(reservationId);
            return "redirect:/manage-students/{studentId}/manage-bookings?success";
        }
        return "redirect:/home?unauthorized";
    }

}

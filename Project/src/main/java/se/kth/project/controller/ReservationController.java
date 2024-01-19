package se.kth.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.project.dto.CourseDTO;
import se.kth.project.dto.ListDTO;
import se.kth.project.dto.UserDTO;
import se.kth.project.model.*;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.CourseService;
import se.kth.project.service.ReservationService;
import se.kth.project.service.UserService;
import se.kth.project.util.CalculateFreeSlotsAndTime;

import java.util.List;

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

    @GetMapping("/reservation-list")
    public String displayReservations(Model model) {
        List<ListEntity> reservationLists = reservationService.getAllLists();
        model.addAttribute("reservationLists", reservationLists);
        int[] nummbeOfAvailableSlots = new int[reservationLists.size()];
        int i = 0;
        for(ListEntity list : reservationLists){
            nummbeOfAvailableSlots[i] = CalculateFreeSlotsAndTime.getAvailableTimeSlots(list.getStart(), list.getIntervall(), list.getS)
        }
        return "reservation-list";
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
        if (SecurityUtil.isUserAdmin()) {
            //get all students that have course access to the course with the corresponding course id
            List<UserDTO> students = userService.retrieveAllStudentsForCourseByListId(listId);
            model.addAttribute("students", students);
            //send in the booking object so that we can list times and slots that are bookable
            Booking booking = reservationService.createBookingObject(listId);
            model.addAttribute("booking", booking);
return "book";
        }
//        String username = SecurityUtil.getSessionUser();
//        if (username != null) {
//            UserEntity user = userService.findByUsername(username);
//            reservationService.bookReservation(listId, user);
//            return "book";
//        }
        return "redirect:/login";
    }

    @PostMapping("/book/save")
    public String saveStudentReservation(@ModelAttribute("booking") Booking booking,
                                         BindingResult result,
                                         Model model) {
        return "redirect:/reservation-list?success";
    }

    @GetMapping("/my-bookings")
    public String showMyBookings(Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            UserEntity user = userService.findByUsername(username);
            List<ReservationEntity> userBookings = reservationService.getReservationsByUserId(user.getId());

            // Log userBookings for debugging
            System.out.println("User Bookings: " + userBookings);

            model.addAttribute("userBookings", userBookings);
            return "book";
        }
        return "redirect:/login";
    }


}

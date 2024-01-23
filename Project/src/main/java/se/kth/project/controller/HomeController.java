package se.kth.project.controller;

import jakarta.servlet.http.HttpSession;
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
import se.kth.project.dto.UserDTO;
import se.kth.project.model.CourseEntity;
import se.kth.project.model.UserEntity;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.CourseService;
import se.kth.project.service.UserService;
import se.kth.project.util.SelectedCourseForm;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final CourseService courseService;


    @Autowired
    public HomeController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/home")
    public String displayHomeAdminPage(HttpSession session, Model model) {
        System.out.println("Printing session user role " + (SecurityUtil.isUserAdmin() ? "admin" : "student"));
        String userRole = SecurityUtil.isUserAdmin() ? "admin" : "student";
        session.setAttribute("userRole", userRole);
        UserDTO user = userService.convertToDTO(userService.findByUsername(SecurityUtil.getSessionUser()));
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/manage-students")
    public String setStudentCourseAccessForm(Model model) {
        if (SecurityUtil.isUserAdmin()) {
            List<UserDTO> students = userService.retrieveAllStudents();
            model.addAttribute("students", students);
            return "manage-students";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    @GetMapping("/manage-students/{studentId}/delete")
    public String deleteStudent(@PathVariable("studentId") Integer studentId, Model model) {
        if (SecurityUtil.isUserAdmin()) {

//            reservationService.deleteReservation(reservationId);
            userService.delete(studentId);
            List<UserDTO> students = userService.retrieveAllStudents();
            model.addAttribute("students", students);
            return "redirect:/manage-students?success=Student deleted";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    @GetMapping("/manage-students/{studentId}/setCourseAccess")
    public String setStudentCourseAccessForm(@PathVariable("studentId") Integer studentId, Model model) {
        if (SecurityUtil.isUserAdmin()) {
            UserDTO user = userService.convertToDTO(userService.findById(studentId));
            model.addAttribute("user", user);

            List<CourseDTO> courses = courseService.getAllCourses();

            model.addAttribute("courses", courses);
            SelectedCourseForm selectedCourse = new SelectedCourseForm();
            selectedCourse.setStudentId(studentId);
            model.addAttribute("selectedCourseForm", selectedCourse); // Initialize an object to hold selected courses
            return "course-access";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    @PostMapping("/course-access/save")
    public String saveStudentCourseAccess(@Valid @ModelAttribute("selectedCourses") SelectedCourseForm selectedCourseForm,
                                          BindingResult result,
                                          Model model) {
        List<Integer> ids = selectedCourseForm.getSelectedCourses();
        //DEBUGGING
//        if (ids != null) {
//            for (Integer id : ids) {
//                System.out.println("Selected course id is: " + id);
//            }
//        } else {
//            System.out.println("No courses selected");
//        }
        if(result.hasErrors()){
            model.addAttribute("user", userService.convertToDTO(userService.findById(selectedCourseForm.getStudentId())));
            model.addAttribute("courses", courseService.getAllCourses());
            SelectedCourseForm selectedCourse = new SelectedCourseForm();
            selectedCourse.setStudentId(selectedCourseForm.getStudentId());
            model.addAttribute("selectedCourseForm", selectedCourse);
            return "course-access";
        }
        Integer studentId = selectedCourseForm.getStudentId();
        List<CourseEntity> selectedCourses = courseService.getCoursesFromIdList(ids);
        UserEntity user = userService.findById(studentId);
        user.setCourses(selectedCourses);
        userService.updateUser(user);
        return "redirect:/manage-students?success=New course access set !";
    }
}

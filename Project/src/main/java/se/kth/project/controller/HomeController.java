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

/**
 * Controller responsible for handling requests related to what is displayed on the home page, and managing students and
 * course access.
 * <p>
 * This controller provides methods for displaying the home page, managing students (e.g., deletion, course access),
 * and handling course access-related actions.
 *
 * @see se.kth.project.service.UserService
 * @see se.kth.project.service.CourseService
 * @see se.kth.project.dto.UserDTO
 * @see se.kth.project.dto.CourseDTO
 * @see se.kth.project.model.UserEntity
 * @see se.kth.project.model.CourseEntity
 * @see se.kth.project.util.SelectedCourseForm
 * @see jakarta.servlet.http.HttpSession
 */
@Controller
public class HomeController {
    private final UserService userService;
    private final CourseService courseService;

    /**
     * Constructs a new instance of the {@code HomeController} class.
     *
     * @param courseService The service responsible for course-related operations.
     * @param userService The service responsible for user-related operations.
     */
    @Autowired
    public HomeController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    /**
     * Displays the home page, setting the user role in the session and providing user information to the model.
     *
     * @param session The HTTP session to set user role information.
     * @param model   The Spring MVC model for rendering the view.
     * @return The view name for the home page.
     */
    @GetMapping("/home")
    public String displayHomePage(HttpSession session, Model model) {
        System.out.println("Printing session user role " + (SecurityUtil.isUserAdmin() ? "admin" : "student"));
        String userRole = SecurityUtil.isUserAdmin() ? "admin" : "student";
        session.setAttribute("userRole", userRole);
        UserDTO user = userService.convertToDTO(userService.findByUsername(SecurityUtil.getSessionUser()));
        model.addAttribute("user", user);
        return "home";
    }

    /**
     * Displays the form for managing students for administrators.
     *
     * @param model The Spring MVC model for rendering the view.
     * @return The view name for the student course access form, or redirect to home if user role is not admin.
     */
    @GetMapping("/manage-students")
    public String displayManageStudentsForm(Model model) {
        if (SecurityUtil.isUserAdmin()) {
            List<UserDTO> students = userService.retrieveAllStudents();
            model.addAttribute("students", students);
            return "manage-students";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    /**
     * Deletes a student based on the provided student ID for administrators.
     *
     * @param studentId The ID of the student to be deleted.
     * @param model     The Spring MVC model for rendering the view.
     * @return The redirect path after deleting the student, or redirect to home without deleting if user role is not admin.
     */
    @GetMapping("/manage-students/{studentId}/delete")
    public String deleteStudent(@PathVariable("studentId") Integer studentId, Model model) {
        if (SecurityUtil.isUserAdmin()) {
            userService.delete(studentId);
            List<UserDTO> students = userService.retrieveAllStudents();
            model.addAttribute("students", students);
            return "redirect:/manage-students?success=Student deleted";
        } else {
            return "redirect:/home?unauthorized";
        }
    }

    /**
     * Displays the form for setting student course access for administrators.
     *
     * @param studentId The ID of the student for whom course access is being set.
     * @param model     The Spring MVC model for rendering the view.
     * @return The view name for the course access form, or redirect to home if user role is not admin.
     */
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

    /**
     * Processes the form submission to save student course access.
     *
     * @param selectedCourseForm The form containing selected courses for a student.
     * @param result             The binding result for validation errors.
     * @param model              The Spring MVC model for rendering the view.
     * @return The redirect path based on the course access saving status and validation results.
     */
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
        if (result.hasErrors()) {
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

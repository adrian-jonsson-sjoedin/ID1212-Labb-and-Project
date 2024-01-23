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
import se.kth.project.model.CourseEntity;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.CourseService;

import java.util.List;

/**
 * Controller handling course-related requests, such as creating and deleting courses.
 * <p>
 * This controller provides methods for displaying the course creation form, processing course creation,
 * and handling course deletion actions. Access to certain functionalities is restricted to administrators.
 *
 * @see se.kth.project.service.CourseService
 * @see se.kth.project.dto.CourseDTO
 * @see se.kth.project.model.CourseEntity
 * @see se.kth.project.security.SecurityUtil
 * @see jakarta.validation.Valid
 * @see org.springframework.ui.Model
 * @see org.springframework.validation.BindingResult
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.ModelAttribute
 * @see org.springframework.web.bind.annotation.PathVariable
 * @see org.springframework.web.bind.annotation.PostMapping
 * @since 1.0
 */
@Controller
public class CourseController {
    private final CourseService courseService;

    /**
     * Constructs a new instance of the {@code CourseController} class.
     *
     * @param courseService The service responsible for course-related operations.
     */
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Displays the course creation form for administrators.
     *
     * @param model The Spring MVC model for rendering the view.
     * @return The view name for the course creation form.
     */
    @GetMapping("/create-course")
    public String createCourseForm(Model model) {
        if (SecurityUtil.isUserAdmin()) {
            CourseDTO course = new CourseDTO();
            model.addAttribute("course", course);
            List<CourseDTO> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            return "create-course";
        } else {
            return "redirect:/home?unauthorized";

        }
    }

    /**
     * Processes the form submission to create a new course.
     *
     * @param course The course data submitted by the user.
     * @param result The binding result for validation errors.
     * @param model  The Spring MVC model for rendering the view.
     * @return The redirect path based on the course creation status and validation results.
     */
    @PostMapping("/create-course/save")
    public String createNewCourse(@Valid @ModelAttribute("course") CourseDTO course,
                                  BindingResult result,
                                  Model model) {
        CourseEntity existingCourse = courseService.findByTitle(course.getTitle());
        if (existingCourse != null && existingCourse.getTitle() != null && !existingCourse.getTitle().isEmpty()) {
            return "redirect:/create-course?fail";
        }
        if (result.hasErrors()) {
            //Need to add the courses again to repopulate the course list in the view
            List<CourseDTO> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("course", course);
            return "create-course";
        }
        courseService.saveCourse(course);
        return "redirect:/create-course?savedSuccess";
    }

    /**
     * Deletes a course based on the provided course ID for administrators.
     *
     * @param courseId The ID of the course to be deleted.
     * @param model    The Spring MVC model for rendering the view.
     * @return The redirect path after deleting the course, or redirect to home without deleting if user role is not admin.
     */
    @GetMapping("/create-course/{courseId}/delete")
    public String deleteCourse(@PathVariable("courseId") Integer courseId, Model model) {
        if (SecurityUtil.isUserAdmin()) {
            int deleteStatus = courseService.delete(courseId);
            if (deleteStatus == 0) {//Successful delete
                //Need to add the courses again to repopulate the course list in the view
                List<CourseDTO> courses = courseService.getAllCourses();
                model.addAttribute("courses", courses);
                return "redirect:/create-course?success";
            } else if (deleteStatus == -1) {
                //Need to add the courses again to repopulate the course list in the view
                List<CourseDTO> courses = courseService.getAllCourses();
                model.addAttribute("courses", courses);
                return "redirect:/create-course?unsuccessful";
            }
        } else {
            return "redirect:/home?unauthorized";
        }
        return "redirect:/home?unauthorized";
    }
}

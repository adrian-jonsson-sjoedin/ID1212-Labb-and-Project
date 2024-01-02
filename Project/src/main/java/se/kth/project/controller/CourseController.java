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
import se.kth.project.model.Course;
import se.kth.project.security.SecurityUtil;
import se.kth.project.service.CourseService;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/create-course")
    public String createCourseForm(Model model, HttpSession session) {
        if (SecurityUtil.isUserAdmin(session)) {
            CourseDTO course = new CourseDTO();
            model.addAttribute("course", course);
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            return "create-course";
        } else {
            return "redirect:/home";

        }
    }

    @PostMapping("/create-course/save")
    public String createNewCourse(@Valid @ModelAttribute("course") CourseDTO course,
                                  BindingResult result,
                                  Model model) {
        Course existingCourse = courseService.findByTitle(course.getTitle());
        if (existingCourse != null && existingCourse.getTitle() != null && !existingCourse.getTitle().isEmpty()) {
            return "redirect:/create-course?fail";
        }
        if (result.hasErrors()) {
            //Need to add the courses again to repopulate the course list in the view
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("course", course);
            return "create-course";
        }
        courseService.saveCourse(course);
        return "redirect:/home?success";
    }

    @GetMapping("/create-course/{courseId}/delete")
    public String deleteCourse(@PathVariable("courseId") Integer courseId, Model model, HttpSession session) {
        if (SecurityUtil.isUserAdmin(session)) {
            courseService.delete(courseId);
            //Need to add the courses again to repopulate the course list in the view
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            return "redirect:/create-course";
        } else {
            return "redirect:/home";
        }
    }
}

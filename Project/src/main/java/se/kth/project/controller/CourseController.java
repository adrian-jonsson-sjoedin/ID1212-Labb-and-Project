package se.kth.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.project.model.Course;
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
    public String createCourseForm(Model model) {
        Course course = new Course();
        model.addAttribute("course", course);
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "create-course";
    }

    @PostMapping("/create-course/save")
    public String createNewCourse(@ModelAttribute("course") Course course) {
        int status = courseService.saveCourse(course);
        if (status == 0) {
            return "redirect:/home?success";
        }else {
            return "redirect:/create-course?null";
        }
    }
}

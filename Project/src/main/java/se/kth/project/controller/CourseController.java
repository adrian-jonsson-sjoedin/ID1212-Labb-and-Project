package se.kth.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.project.dto.CourseDTO;
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
        CourseDTO course = new CourseDTO();
        model.addAttribute("course", course);
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "create-course";
    }

    @PostMapping("/create-course/save")
    public String createNewCourse(@Valid @ModelAttribute("course") CourseDTO course,
                                  BindingResult result,
                                  Model model) {

        if(result.hasErrors()){
            //Need to add the courses again to repopulate the course list in the view
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);

            return "create-course";
        }
        courseService.saveCourse(course);
        return "redirect:/home?success";
    }
}

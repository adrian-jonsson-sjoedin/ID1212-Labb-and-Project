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
import se.kth.project.service.UserService;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

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

package se.kth.project.service;

import se.kth.project.dto.CourseDTO;
import se.kth.project.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    void saveCourse(CourseDTO courseDTO);
    Course findByTitle(String title);
}

package se.kth.project.service;

import se.kth.project.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    int saveCourse(Course newCourse);
}

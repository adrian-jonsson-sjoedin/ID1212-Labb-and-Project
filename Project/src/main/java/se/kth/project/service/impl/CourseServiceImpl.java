package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.CourseDTO;
import se.kth.project.model.Course;
import se.kth.project.repository.CourseRepository;
import se.kth.project.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void saveCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        courseRepository.save(course);
    }

    @Override
    public Course findByTitle(String title) {
        return courseRepository.findByTitle(title);
    }
}

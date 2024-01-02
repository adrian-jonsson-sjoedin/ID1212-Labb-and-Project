package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.CourseDTO;
import se.kth.project.model.Course;
import se.kth.project.repository.CourseRepository;
import se.kth.project.service.CourseService;

import java.util.List;

/**
 * Service implementation for handling operations related to courses.
 * <p>
 * This service provides methods for CRUD operations on courses.
 */
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    /**
     * Constructs a new instance of the {@code CourseServiceImpl} class.
     *
     * @param courseRepository The repository for course-related database operations.
     */
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves a list of all courses from the database.
     *
     * @return A list of all courses.
     */
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Saves a new course to the database based on the provided course DTO.
     *
     * @param courseDTO The DTO containing course information to be saved.
     */
    @Override
    public void saveCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        courseRepository.save(course);
    }

    /**
     * Finds a course in the database based on its title.
     *
     * @param title The title of the course to be retrieved.
     * @return The course with the specified title, or {@code null} if not found.
     */
    @Override
    public Course findByTitle(String title) {
        return courseRepository.findByTitle(title);
    }

    @Override
    public void delete(Integer courseId) {
        courseRepository.deleteById(courseId);
    }
}

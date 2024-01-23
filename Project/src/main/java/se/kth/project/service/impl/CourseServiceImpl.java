package se.kth.project.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.CourseDTO;
import se.kth.project.model.CourseEntity;
import se.kth.project.model.ListEntity;
import se.kth.project.repository.CourseRepository;
import se.kth.project.repository.ListRepository;
import se.kth.project.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation class for managing courses.
 * Provides methods for retrieving, saving, and deleting courses.
 *
 * @see CourseService
 */
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ListRepository listRepository;

    /**
     * Constructs a new instance of the {@code CourseServiceImpl} class.
     *
     * @param courseRepository The repository for course-related database operations.
     * @param listRepository   The repository for list-related database operations.
     */
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, ListRepository listRepository) {
        this.courseRepository = courseRepository;
        this.listRepository = listRepository;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseDTO> getAllCourses() {
        List<CourseEntity> courses = courseRepository.findAll();
        return courses.stream().map(this::convertToCourseDTO).collect(Collectors.toList());
    }

    /**
     * Converts a {@link CourseEntity} to a {@link CourseDTO}.
     *
     * @param course The course entity to be converted.
     * @return The corresponding course DTO.
     */
    private CourseDTO convertToCourseDTO(CourseEntity course) {
        return new CourseDTO(
                course.getId(),
                course.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCourse(CourseDTO courseDTO) {
        CourseEntity course = new CourseEntity();
        course.setTitle(courseDTO.getTitle());
        courseRepository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseEntity findByTitle(String title) {
        return courseRepository.findByTitle(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseEntity findById(Integer id) {
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(id);
        return courseEntityOptional.orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseEntity> getCoursesFromIdList(List<Integer> courseIdList) {
        List<CourseEntity> selectedCourses = new ArrayList<>();
        for (Integer courseId : courseIdList) {
            // Use Optional to handle the possibility of a null result
            Optional<CourseEntity> courseOptional = courseRepository.findById(courseId);
            // Check if the course was found and present in the repository
            courseOptional.ifPresent(selectedCourses::add);
        }
        return selectedCourses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int delete(Integer courseId) {
        ListEntity list = listRepository.findByCourse_Id(courseId);
        if (list == null) {
            courseRepository.deleteByCourseId(courseId);
            courseRepository.deleteById(courseId);
            return 0;
        } else {
            return -1;
        }
    }
}

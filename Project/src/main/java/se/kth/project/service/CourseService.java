package se.kth.project.service;

import se.kth.project.dto.CourseDTO;
import se.kth.project.model.CourseEntity;
import se.kth.project.service.impl.CourseServiceImpl;

import java.util.List;

/**
 * Service interface for managing courses.
 * Defines methods for retrieving, saving, and deleting courses.
 *
 * @see CourseServiceImpl
 */
public interface CourseService {
    /**
     * Retrieves a list of all courses from the database.
     *
     * @return A list of all courses.
     */
    List<CourseDTO> getAllCourses();

    /**
     * Saves a new course to the database based on the provided course DTO.
     *
     * @param courseDTO The DTO containing course information to be saved.
     */
    void saveCourse(CourseDTO courseDTO);

    /**
     * Finds a course in the database based on its title.
     *
     * @param title The title of the course to be retrieved.
     * @return The course with the specified title, or {@code null} if not found.
     */
    CourseEntity findByTitle(String title);

    /**
     * Deletes a course.
     * This method is transactional, and it deletes related data from the database.
     * If the course is associated with a list, the deletion is not allowed.
     *
     * @param courseId The ID of the course to be deleted.
     * @return 0 if the deletion is successful, -1 if the course is associated with a list.
     */
    int delete(Integer courseId);

    /**
     * Finds a course in the database based on its ID.
     *
     * @param id The ID of the course to be retrieved.
     * @return The course with the specified ID, or {@code null} if not found.
     */
    CourseEntity findById(Integer id);

    /**
     * Retrieves a list of courses based on a list of course IDs.
     *
     * @param courseIdList The list of course IDs.
     * @return The list of corresponding course entities.
     */
    List<CourseEntity> getCoursesFromIdList(List<Integer> courseIdList);
}

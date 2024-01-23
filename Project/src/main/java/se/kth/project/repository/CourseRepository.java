package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.model.CourseEntity;

/**
 * Repository interface for managing {@link se.kth.project.model.CourseEntity} entities.
 * Provides CRUD operations and custom queries for courses.
 */
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    /**
     * Retrieves a course entity by its title.
     *
     * @param title The title of the course.
     * @return The course entity with the specified title, or {@code null} if not found.
     */
    CourseEntity findByTitle(String title);

    /**
     * Deletes course access entries associated with the given course ID.
     *
     * @param courseId The ID of the course for which course access entries should be deleted.
     */
    @Modifying
    @Query(value = "DELETE FROM course_access WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourseId(@Param("courseId") int courseId);
}

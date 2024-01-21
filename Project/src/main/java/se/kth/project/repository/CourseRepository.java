package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.model.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    CourseEntity findByTitle(String title);

    @Modifying
    @Query(value = "DELETE FROM course_access WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourseId(@Param("courseId") int courseId);
}

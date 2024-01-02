package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    CourseEntity findByTitle(String title);

}

package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

}

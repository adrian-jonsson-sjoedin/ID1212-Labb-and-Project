package se.kth.project.service;

import se.kth.project.dto.CourseDTO;
import se.kth.project.model.CourseEntity;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();

    void saveCourse(CourseDTO courseDTO);

    CourseEntity findByTitle(String title);

    int delete(Integer courseId);

    CourseEntity findById(Integer id);

    List<CourseEntity> getCoursesFromIdList(List<Integer> courseIdList);
}

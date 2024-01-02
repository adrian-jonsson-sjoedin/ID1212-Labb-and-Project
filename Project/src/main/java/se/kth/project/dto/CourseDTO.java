package se.kth.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * A DTO used when creating new courses.
 */
@Data
public class CourseDTO {
    private Integer courseId;
    @NotEmpty(message = "Must enter course code and course name.")
    private String title;
}

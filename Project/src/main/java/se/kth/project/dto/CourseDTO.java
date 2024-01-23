package se.kth.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Integer courseId;
    @NotEmpty(message = "Must enter course code and course name.")
    private String title;


}

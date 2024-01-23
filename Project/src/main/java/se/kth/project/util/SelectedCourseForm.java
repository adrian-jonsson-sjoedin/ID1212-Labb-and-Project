package se.kth.project.util;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Used solely to store the selected courses on the course-access page
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedCourseForm {
    @NotNull(message = "You must set at least one course access")
    @NotEmpty(message = "You must set at least one course access")
    private List<Integer> selectedCourses;
    private Integer studentId;
}

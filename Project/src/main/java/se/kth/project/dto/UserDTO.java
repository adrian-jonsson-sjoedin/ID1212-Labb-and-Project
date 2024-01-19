package se.kth.project.dto;

import lombok.Builder;
import lombok.Data;
import se.kth.project.model.CourseEntity;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private Integer userId;
    private String username;
    private boolean admin;
    private List<CourseEntity> courses;
}

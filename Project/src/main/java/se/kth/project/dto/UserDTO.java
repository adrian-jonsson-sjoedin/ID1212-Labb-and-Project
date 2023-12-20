package se.kth.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer userId;
    private String username;
    private boolean admin;
}

package se.kth.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
//@Builder
public class RegistrationDTO {
    private Integer userId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotNull
    private boolean admin;
}

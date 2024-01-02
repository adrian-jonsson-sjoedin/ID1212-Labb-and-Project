package se.kth.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
//@Builder
public class RegistrationDTO {
    private Integer userId;
    @NotEmpty(message = "You must provide a username.")
    private String username;
    @NotEmpty(message = "You must provide a password.")
    private String password;
    @NotNull(message = "You must select if the new user is an admin or not.")
    private boolean admin;
}

package se.kth.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.project.dto.RegistrationDTO;
import se.kth.project.model.UserEntity;
import se.kth.project.service.UserService;

/**
 * Controller handling authentication-related requests, such as login and registration of new user.
 * <p>
 * This controller provides methods for displaying login and registration pages, processing user registration,
 * and handling redirection based on authentication status.
 */
@Controller
public class AuthenticationController {
    private UserService userService;

    /**
     * Constructs a new instance of the {@code AuthenticationController} class.
     *
     * @param userService The service responsible for user-related operations.
     */
    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Redirects to the login page.
     *
     * @return The redirect path to the login page.
     */
    @GetMapping("/")
    public String redirectToLogin() {

        return "redirect:/login";
    }

    /**
     * Displays the login page.
     *
     * @return The view name for the login page.
     */
    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }

    /**
     * Retrieves the registration form and add a RegistrationDTO object to the model to be used for creating a new user.
     *
     * @param model The Spring MVC model for rendering the view.
     * @return The view name for the registration form.
     */
    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDTO user = new RegistrationDTO();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Processes user registration.
     * <p>
     * This method handles the registration form submission, validates the form data, and performs the user registration.
     * Redirects to different pages based on the registration status and input validation.
     *
     * @param user   The registration data submitted by the user.
     * @param result The binding result for validation errors.
     * @param model  The Spring MVC model for rendering the view.
     * @return The redirect path based on the registration status and validation results.
     */
    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDTO user,
                           BindingResult result, Model model) {
        UserEntity existingUsername = userService.findByUsername(user.getUsername());
        if (existingUsername != null && existingUsername.getUsername() != null && !existingUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        int status = userService.saveUser(user);
        if (status == 0) {
            return "redirect:/home?success";
        } else {
            return "redirect:/register?unauthorized";
        }
    }
}

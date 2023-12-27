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
import se.kth.project.security.CustomUserDetailsService;
import se.kth.project.service.UserService;

@Controller
public class AuthenticationController {
    private UserService userService;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String redirectToLogin() {

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDTO user = new RegistrationDTO();
        model.addAttribute("user", user);
        return "register";
    }


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
        }else {
            return "redirect:/register?unauthorized";
        }
    }

//    @PostMapping("/login")
//    public String handleLogin(@RequestParam("username") String username,
//                              @RequestParam("password") String password,
//                              Model model,
//                              HttpSession session) {
//        UserDTO user = userService.retrieveUser(username, password);
//        if (user != null && user.isAdmin()) {
//            System.out.println(user.getUsername());
//            session.setAttribute("user", user);
////            model.addAttribute("user", user);
//            return "redirect:/home-admin";
//        }else if(user != null && !user.isAdmin()){
//            session.setAttribute("user", user);
//            return "redirect:/home-student";
//        }else {
//            model.addAttribute("LoginStatus", "notFound");
//            return "login";
//        }
//    }

}

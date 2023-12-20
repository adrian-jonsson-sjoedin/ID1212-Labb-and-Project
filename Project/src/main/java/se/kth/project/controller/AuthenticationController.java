package se.kth.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.kth.project.dto.UserDTO;
import se.kth.project.service.UserService;

@Controller
//@RequestMapping("")
public class AuthenticationController {
    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String redirectToLogin() {

        return "redirect:/login";
    }
    @GetMapping("/login")
    public String displayLoginForm(){
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model,
                              HttpSession session) {
        UserDTO user = userService.retrieveUser(username, password);
        if (user != null && user.isAdmin()) {
            System.out.println(user.getUsername());
            session.setAttribute("user", user);
//            model.addAttribute("user", user);
            return "redirect:/home-admin";
        }else if(user != null && !user.isAdmin()){
            session.setAttribute("user", user);
            return "redirect:/home-student";
        }else {
            model.addAttribute("LoginStatus", "notFound");
            return "login";
        }
    }
//    @GetMapping("/logout")
//    public String handleLogout(HttpSession session, HttpServletResponse response){
//        session.invalidate();
//        // Add cache control headers to prevent browser caching
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
//        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
//        response.setHeader("Expires", "0"); // Proxies.
//        return "redirect:/login";
//    }
}

package se.kth.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import se.kth.project.security.SecurityUtil;

@Controller
public class HomeController {


    @GetMapping("/home")
    public String displayHomeAdminPage(HttpSession session){
        System.out.println("Printing session user role " + SecurityUtil.getSessionUserRole());
        String userRole = SecurityUtil.getSessionUserRole();
        session.setAttribute("userRole", userRole);
        return "home";
    }
}

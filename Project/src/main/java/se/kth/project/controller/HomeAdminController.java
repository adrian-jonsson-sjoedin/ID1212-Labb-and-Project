package se.kth.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home-admin")
public class HomeAdminController {


    @GetMapping
    public String displayHomeAdminPage(){
        return "home-admin";
    }
}

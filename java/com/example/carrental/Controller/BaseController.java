package com.example.carrental.Controller;

import com.example.carrental.Models.client.ClientModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

    @GetMapping({ "/"})
    public String landingPage(){
        return "landing_page";
    }

    @GetMapping({ "/login"})
    public String showLoginForm(){
        return "auth/login_client";
    }

    @GetMapping({ "/register"})
    public String showRegisterForm(Model model){
        model.addAttribute("client", new ClientModel());
        return "auth/register_client";
    }

}
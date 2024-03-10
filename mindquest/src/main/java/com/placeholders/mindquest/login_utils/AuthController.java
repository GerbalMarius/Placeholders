package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.user_utils.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String authPage(){
        return "auth";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);

        return "register";
    }
}

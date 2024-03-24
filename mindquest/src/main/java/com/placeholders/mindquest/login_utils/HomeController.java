package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.user_utils.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){

        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }

        var user = AuthController.currentUser()
                .map(data -> new UserDTO(data.getId(), data.getFirstName(), data.getLastName(), data.getEmail()))
                .orElse(new UserDTO());

        model.addAttribute("currentUser", user);

        return "dashboard";
    }
}

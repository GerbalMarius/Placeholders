package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.user_utils.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){

        if (!AuthController.isLoggedIn() && AuthController.firstTimeUser().isEmpty()){
            return "redirect:/login?please";
        }

        var user = getUser();


        model.addAttribute("currentUser", user);

        if (AuthController.firstTimeUser().isPresent()){
            model.addAttribute("firstTime",true);
            return "dashboard";
        }


        return "dashboard";
    }

    private static UserDTO getUser() {
        if (AuthController.firstTimeUser().isPresent()){
            return AuthController.firstTimeUser().get();
        }
        else if (AuthController.currentUser().isPresent()){

            return AuthController.currentUser()
                    .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                    .orElse(new UserDTO());
        }
        throw new NoSuchElementException("No users currently present!");
    }


}

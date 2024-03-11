package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public String authPage(){
        return "auth";
    }

    //add user data to form register page
    @GetMapping("/register")
    public String showRegisterPage(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);

        return "register";
    }


    //To save new user
    //And validate for existing users
    @PostMapping("/register/save")
    public String saveRegisteredUser(@Valid @ModelAttribute("user") UserDTO userData, BindingResult authResult, Model model){

        User existingUser = userService.findUserByEmail(userData.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            authResult.reject("email", null, "There is already an registered user with the same email.");
        }

        if (authResult.hasErrors()){
            model.addAttribute("user", userData);
            return "register";
        }

        userService.saveUser(userData);

        return "redirect:/register?success";
    }



    //to form login page
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    //handler method to add all users to our model
    @GetMapping("/users")
    public String showAllUsers(Model model){

        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
}

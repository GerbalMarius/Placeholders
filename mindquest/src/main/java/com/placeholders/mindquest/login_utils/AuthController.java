package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;

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
            model.addAttribute("message", "User already exists");
            return "/register";
        }

        userService.saveUser(userData);

        return "redirect:/register?success";
    }


    //to form login page
    @GetMapping("/login")
    public String loginPage(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "login";
    }

    @GetMapping("/login/check")
    public String validateExistingUser( @Valid @ModelAttribute("user") UserDTO userData, BindingResult authResult, Model model){
        User userToFind = userService.findUserByEmail(userData.getEmail());

        List<Role> userRoles = new ArrayList<>();

        if (userToFind == null || userToFind.getEmail() == null || userToFind.getEmail().isEmpty()
                || !userService.isValidPassword(userToFind, userData)) {
            authResult.reject("email", null, "Invalid email or password");
        }

        if (userToFind != null){
            userRoles = userToFind.getRoles();
        }


        if (authResult.hasErrors()){
            model.addAttribute("message", "Invalid userName or password");
            return "redirect:/login?error";
        }


        return userRoles.stream().anyMatch(role -> role.getName().contains("ADMIN"))
                ? "redirect:/users" :
                "redirect:/login?success";
    }
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/login?logout";
    }



    //handler method to add all users to our model
    @GetMapping("/users")
    public String showAllUsers(Model model){

        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
}

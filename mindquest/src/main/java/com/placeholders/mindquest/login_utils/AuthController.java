package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    private static User currentUser = null;
    private static UserDTO firstTimeUser = null;



    public AuthController(UserService userService){
        this.userService = userService;

    }


    //add user data to form register page
    @GetMapping("/register")
    public String showRegisterPage(Model model){

        if (AuthController.isLoggedIn()){
            return "redirect:/mindboard";
        }

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
            logger.info("Rejected register request");
            return "/register";
        }

        User savedUser = userService.saveUser(userData);
        logger.info("Registration successful for user: " + userData.getEmail());

        firstTimeUser = new UserDTO(userData.getId(), userData.getFirstName(), userData.getLastName(), userData.getEmail());
        currentUser = savedUser;

        return savedUser.isAdmin() ? "redirect:/users" : "redirect:/mindboard";
    }


    //to form login page
    @GetMapping("/login")
    public String loginPage(Model model){

        if (AuthController.isLoggedIn()){
            return "redirect:/mindboard";
        }

        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "login";
    }

    @GetMapping("/login/check")
    public String validateExistingUser( @Valid @ModelAttribute("user") UserDTO userData, BindingResult authResult, Model model){

        if (AuthController.isLoggedIn()){
            return "redirect:/mindboard";
        }
      
        User userToFind = userService.findUserByEmail(userData.getEmail());
        boolean isAdmin = false;

        if (userToFind == null || userToFind.getEmail() == null || userToFind.getEmail().isEmpty()
                || !userService.isValidPassword(userToFind, userData)) {
            authResult.reject("email", null, "Invalid email or password");
        }

        if (userToFind != null){
            isAdmin = userToFind.isAdmin();
        }


        if (authResult.hasErrors()){
             model.addAttribute("message", "Invalid userName or password");
            logger.info("Rejected login request");
            return "redirect:/login?error";
        }


        currentUser = userToFind;
      


        logger.info("Login successful for user: " + userData.getEmail() + " " + (isAdmin ? "ADMIN" : " "));
        return  isAdmin ? "redirect:/users" : "redirect:/mindboard";
    }

    @GetMapping("/logout")
    public String logout(){
        currentUser = null;
        firstTimeUser = null;
        logger.info("Session has been cleared.");
        return "redirect:/login?logout";
    }



    //handler method to add all users to our model
    @GetMapping("/users")
    public String showAllUsers(Model model){

        if (currentUser == null){
            return "redirect:/login?please";
        }

        if (!currentUser.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Normal users can't have administrative privileges");
        }

        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("current", currentUser);
        return "users";
    }

    public static Optional<User> currentUser(){
        return Optional.ofNullable(currentUser);
    }

    public static void setCurrentUser(User currentUser) {
        AuthController.currentUser = currentUser;
    }

    public static Optional<UserDTO> firstTimeUser(){
        return Optional.ofNullable(firstTimeUser);
    }

    public static void setFirstTimeUser(UserDTO firstTimeUser) {
        AuthController.firstTimeUser = firstTimeUser;
    }

    public static boolean isLoggedIn(){
        return currentUser().isPresent();
    }
}

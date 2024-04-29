package com.placeholders.mindquest.Settingscontrollers;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import com.placeholders.mindquest.Settingsmodels.ProfilePhotoRepository;
import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PutMapping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@Controller
public class SettingsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDTO tempUser = new UserDTO(new Random().nextLong(0,1000), "Jonas", "Jonaitis", "jonas.jonaitis@gmail.com");

    //TODO ORGANIZE PAGES ON A LATER DATE (TEMP SOLUTION FOR NOW.)
    @GetMapping("/settings")
    public String settingsPage(Model model){
        Optional<User> user = AuthController.currentUser();
        ProfilePhoto photo = profilePhotoRepository.findById(user.get().getId());
        if(user.isPresent())
        {
            model.addAttribute("user",user);
        }
        if(photo != null)
        {
            byte[] profilePhotoData = photo.getData();
            String base64ImageData = Base64.getEncoder().encodeToString(profilePhotoData);
            model.addAttribute("photo",photo);
            model.addAttribute("profilePhotoData", base64ImageData);
        }
        return "settings";
    }

    @GetMapping("/settings/change_user_name")
    public String userNamePage(){
        return "update-first-name";
    }
    @GetMapping("/settings/change_password")
    public String passwordPage(){
        return "update-password";
    }
    @GetMapping("/settings/change_last_name")
    public String lastNamePage(){
        return "update-last-name";
    }
    @GetMapping("/settings/pfp_upload")
    public String pfpUpload(){
        return "upload-picture";
    }


    @PostMapping("/settings/updatePassword")
    public String updatePassword(@RequestParam String newPassword, Model model) {
        Optional<User> currentUser = AuthController.currentUser();
        if(currentUser.isPresent() && newPassword != null) {
            User user = userRepository.findByEmail(currentUser.get().getEmail());
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            model.addAttribute("message", "Password was updated successfully");
            return "redirect:/settings?password";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter a new password, do not leave the field empty");
        }
    }
    @PostMapping("/settings/updateFirstName")
    public String updateFirstName(@RequestParam String firstName, Model model) {
        Optional<User> currentUser = AuthController.currentUser();
        if(currentUser.isPresent()) {
            User user = userRepository.findByEmail(currentUser.get().getEmail());
            user.setFirstName(firstName);
            userRepository.save(user);
            model.addAttribute("message", "Name was changed successfully");
            return "redirect:/settings?name";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New user name can't be empty");
        }
    }
    @PostMapping("settings/updateLastName")
    public String updateLastName(@RequestParam String lastName, Model model) {
        Optional<User> currentUser = AuthController.currentUser();
        if(currentUser.isPresent()) {
            User user = userRepository.findByEmail(currentUser.get().getEmail());
            user.setLastName(lastName);
            userRepository.save(user);
            model.addAttribute("message", "LastName was changed succesfully");
            return "redirect:/settings?last_name";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New LastName can't be empty");
        }
    }
    @PostMapping("/settings/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture, Model model) {

        Optional<User> currentUser = AuthController.currentUser();
        if(profilePicture != null && !profilePicture.isEmpty() && currentUser.isPresent()) {
            try {
                ProfilePhoto photo = new ProfilePhoto();
                photo.setId(currentUser.get().getId());
                photo.setData(profilePicture.getBytes());
                int photoSize = photo.getData().length;
                if (photoSize < 100000)
                {
                    profilePhotoRepository.save(photo);
                    model.addAttribute("message", "Picture changed successfully");
                    return "redirect:/settings?profile_picture";
                }
                model.addAttribute("error", "Failed to upload picture." +" " + "The photo is too fat boi");
                return "redirect:/settings?error";
            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload picture." +" " + e.getMessage());
                return "redirect:/settings?error";
            }
        } else {
            model.addAttribute("pictureIsEmpty", "Picture update window is empty");
            return "redirect:/settings?empty_picture";
        }
    }

}

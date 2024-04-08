package com.placeholders.mindquest.Settingscontrollers;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import com.placeholders.mindquest.user_utils.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Random;

@Controller
public class SettingsController {
    private ProfilePhoto photo = new ProfilePhoto("1","pfp.jpg");
    private UserDTO tempUser = new UserDTO(new Random().nextLong(0,1000), "Jonas", "Jonaitis", "jonas.jonaitis@gmail.com");

    //TODO ORGANIZE PAGES ON A LATER DATE (TEMP SOLUTION FOR NOW.)
    @GetMapping("/settings")
    public String settingsPage(){
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


    @PutMapping("settings/{id}/updatePassword")
    public String updatePassword(@PathVariable long id, @RequestParam String newPassword, Model model) {
        if(newPassword != null && !newPassword.isEmpty()) {
            tempUser.setPassword(newPassword);
            model.addAttribute("message", "Password was updated successfully");
            return "redirect:/settings?password";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter a new password, do not leave the field empty");
        }
    }
    @PutMapping("settings/{id}/updateFirstName")
    public String updateFirstName(@PathVariable long id, @RequestParam String firstName, Model model) {
        if(firstName != null && !firstName.isEmpty()) {
            tempUser.setFirstName(firstName);
            model.addAttribute("message", "Name was changed successfully");
            return "redirect:/settings?name";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New user name can't be empty");
        }
    }
    @PutMapping("settings/{id}/updateLastName")
    public String updateLastName(@PathVariable long id, @RequestParam String lastName, Model model) {
        if(lastName != null && !lastName.isEmpty()) {
            tempUser.setLastName(lastName);
            model.addAttribute("message", "Password was changed succesfully");
            return "redirect:/settings?last_name";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password can't be empty");
        }
    }
    @PostMapping("settings/{id}/uploadProfilePicture")
    public String uploadProfilePicture(@PathVariable long id, @RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
        if(profilePicture != null && !profilePicture.isEmpty()) {
            try {
                photo.setData(profilePicture.getBytes());
                model.addAttribute("message", "Picture changed successfully");
                return "redirect:/settings?profile_picture";
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

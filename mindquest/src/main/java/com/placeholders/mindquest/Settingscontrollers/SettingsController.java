package com.placeholders.mindquest.Settingscontrollers;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import com.placeholders.mindquest.Settingsmodels.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class SettingsController {
    private ProfilePhoto photo = new ProfilePhoto("1","pfp.jpg");
    private User user = new User("teemo","Vardenis","Pavardenis","var.pavard@gmail.com","boop123",photo);
    @GetMapping("/Settings/{id}")
    public User getUser(@PathVariable String id)
    {
        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return user;
    }
    @PutMapping("/Settings/{id}/updatePassword")
    public String updatePassword(@PathVariable String id, @RequestParam String newPassword) {
        if(newPassword != null && !newPassword.isEmpty()) {
            user.setPassWord(newPassword);
            return "Slaptazodis pakeistas";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iveskite nauja slaptazodi, nepalikti tuscio langelio");
        }
    }
    @PutMapping("/Settings/{id}/updateFirstName")
    public String updateFirstName(@PathVariable String id, @RequestParam String firstName) {
        if(firstName != null && !firstName.isEmpty()) {
            user.setFirstName(firstName);
            return "Vardas pakeistas";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iveskite varda, nepalikite tuscio langelio");
        }
    }
    @PutMapping("/Settings/{id}/updateLastName")
    public String updateLastName(@PathVariable String id, @RequestParam String lastName) {
        if(lastName != null && !lastName.isEmpty()) {
            user.setLastName(lastName);
            return "Pavarde pakeista";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iveskite pavarde, nepalikti tuscio langelio");
        }
    }
    @PostMapping("/Settings/{id}/uploadProfilePicture")
    public String uploadProfilePicture(@PathVariable String id, @RequestParam("profilePicture") MultipartFile profilePicture) {
        if(profilePicture != null && !profilePicture.isEmpty()) {
            try {
                photo.setData(profilePicture.getBytes());
                return "Profilio paveikslelis pakeistas";
            } catch (IOException e) {
                // Handle exceptions if any
                return "Nepavyko ikelti profilio paveikslelio " + e.getMessage();
            }
        } else {
            return "Ikelkite nuotrauka";
        }
    }
}

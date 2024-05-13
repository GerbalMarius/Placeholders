package com.placeholders.mindquest.settings;

import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;


@Controller
public class SettingsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/settings")
    public String settingsPage(Model model){
        Optional<User> user = AuthController.currentUser();
        ProfilePhoto photo = profilePhotoRepository.findById(user.get().getId());
        model.addAttribute("user", user);
        if(photo != null)
        {
            byte[] profilePhotoData = photo.getData();
            String base64ImageData = Base64.getEncoder().encodeToString(profilePhotoData);
            model.addAttribute("photo",photo);
            model.addAttribute("profilePhotoData", base64ImageData);
        }
        return "settings";
    }

    @GetMapping("/settings/change_first_name")
    public String userNamePage(){
        return "change-first-name";
    }
    @GetMapping("/settings/change_password")
    public String passwordPage(){
        return "change-password";
    }
    @GetMapping("/settings/change_last_name")
    public String lastNamePage(){
        return "change-last-name";
    }
    @GetMapping("/settings/change_profile_picture")
    public String pfpUpload(){
        return "change-profile-picture";
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
            currentUser.get().setFirstName(firstName);
            User user = userRepository.findByEmail(currentUser.get().getEmail());
            user.setFirstName(firstName);
            userRepository.save(user);
            model.addAttribute("message", "Name was changed successfully");
            return "redirect:/settings?name";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New user name can't be empty");
        }
    }
    @PostMapping("/settings/updateLastName")
    public String updateLastName(@RequestParam String lastName, Model model) {
        Optional<User> currentUser = AuthController.currentUser();
        if(currentUser.isPresent()) {
            currentUser.get().setLastName(lastName);
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

                byte[] compressedImageData = compressImage(profilePicture.getBytes(), 250, 250);
                if (compressedImageData == null) {
                    return "redirect:/settings?error=Failed+to+upload+picture+try+another+image,+recommnded+to+use+.png+format";
                }
                photo.setData(compressedImageData);

                profilePhotoRepository.save(photo);
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

    // Method to compress image
    private byte[] compressImage(byte[] imageData, int targetWidth, int targetHeight) throws IOException {

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        if (originalImage == null) {
            return null;
        }
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

}
package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.settings.ProfilePhoto;
import com.placeholders.mindquest.settings.ProfilePhotoRepository;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/mindboard")
    public String mindboard(Model model){

        if (!AuthController.isLoggedIn() && AuthController.firstTimeUser().isEmpty()){
            return "redirect:/login?please";
        }
        var user = getUser();


        model.addAttribute("currentUser", user);

        boolean isFirstTime = AuthController.firstTimeUser().isPresent();

        model.addAttribute("startingQuizNotTaken",isFirstTime);

        Optional<User> currentUser = AuthController.currentUser();
        ProfilePhoto photo = profilePhotoRepository.findById(currentUser.get().getId());
        if(photo != null)
        {
            byte[] profilePhotoData = photo.getData();
            String base64ImageData = Base64.getEncoder().encodeToString(profilePhotoData);
            model.addAttribute("photo",photo);
            model.addAttribute("profilePhotoData", base64ImageData);
        }

        return "mindboard";
    }

    private static UserDTO getUser() {
        if (AuthController.firstTimeUser().isPresent()){
            return AuthController.firstTimeUser().get();
        }
        else if (AuthController.currentUser().isPresent()){

            return AuthController.currentUser()
                    .map(User::getTransferableData)
                    .orElse(new UserDTO());
        }
        throw new NoSuchElementException("No users currently present!");
    }


}

package com.placeholders.mindquest.quiz_history;


import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HistoryController {

    @Autowired
    private UserService userService;


    @GetMapping("/dashboard/history")
    public String getQuizHistory(Model model){

        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }

        Optional<User> currUser = AuthController.currentUser();

        currUser.ifPresent(user -> {
            final User actual = userService.findUserByEmail(user.getEmail());
            model.addAttribute("user", actual);
            model.addAttribute("timeStamps", actual.getTimestampLog());
        });

        return "quiz-history";
    }
}
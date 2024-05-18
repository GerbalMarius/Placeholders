package com.placeholders.mindquest.quiz_history;


import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.mindfeed.Post;
import com.placeholders.mindquest.mindfeed.PostService;
import com.placeholders.mindquest.timestamp.TimeStamp;
import com.placeholders.mindquest.timestamp.TimeStampService;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HistoryController {

    @Autowired
    private UserService userService;

    private final TimeStampService timeStampService;

    @Autowired
    public HistoryController(TimeStampService timeStampService) {
        this.timeStampService = timeStampService;
    }

    @GetMapping("/quiz/history")
    public String getQuizHistory(Model model, @RequestParam(defaultValue = "0") int page){

        if (!AuthController.isLoggedIn()){
            return "redirect:/login?please";
        }

        Optional<User> currUser = AuthController.currentUser();

        currUser.ifPresent(user -> {
            final User actual = userService.findUserByEmail(user.getEmail());
            model.addAttribute("user", actual);

            int pageSize = 5;

            List<TimeStamp> timeStamps = timeStampService.getPostsPagedLatest(page, pageSize, actual);

            model.addAttribute("timeStamps", timeStamps);
            model.addAttribute("currentPage", page);


            long totalPages = timeStampService.getTotalPageCount(pageSize, actual);
            if (totalPages < 1) {
                totalPages = 1;
            }
            model.addAttribute("totalPages", totalPages);
        });

        return "quiz-history";
    }
}
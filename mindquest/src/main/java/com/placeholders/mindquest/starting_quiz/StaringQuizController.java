package com.placeholders.mindquest.starting_quiz;

import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class StaringQuizController {

    private final StartingQuizRepository startingQuizRepository;

    private final UserRepository userRepository;

    public StaringQuizController(StartingQuizRepository startingQuizRepository, UserRepository userRepository) {
        this.startingQuizRepository = startingQuizRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/starting_quiz")
    public String startingQuiz(Model model) {

        model.addAttribute("quizInfo", new StartingQuizInfo());
        return "starting-quiz";
    }

    @PostMapping("/starting-quiz/results")
    public String SubmitQuiz(@ModelAttribute StartingQuizInfo startingQuizInfo, Model model) {

        if (!AuthController.isLoggedIn()){
            return "redirect:/register";
        }

        // Calculates mental state and prints info that is submitted
        String mentalState = calculateMentalState(startingQuizInfo);
        System.out.println("Submitted quiz info: " + startingQuizInfo);

        UserDTO user = AuthController.currentUser().get().getTransferableData();
        final User actualUser = userRepository.findByEmail(user.getEmail());
        actualUser.setStartingQuizInfo(startingQuizInfo);
        startingQuizInfo.setUser(actualUser);
        startingQuizRepository.save(startingQuizInfo);


        AuthController.setCurrentUser(actualUser);

        // Returns a view with the submitted data and mental state
        model.addAttribute("quizInfo", startingQuizInfo);
        model.addAttribute("mentalState", mentalState);

        return "starting-quiz-result";
    }

    public String calculateMentalState(StartingQuizInfo startingQuizInfo) {

        int stressScore = calculateStressScore(startingQuizInfo.getStressLevel(), startingQuizInfo.getHowOftenFeelTired(), startingQuizInfo.getFulfillment());
        int sleepScore = calculateSleepScore(startingQuizInfo.getRatingOfSleep(), startingQuizInfo.getHoursOfSleep(), startingQuizInfo.getHardToSleep());
        int activityScore = calculateActivityScore(startingQuizInfo.getActivePerWeek());

        int totalScore = stressScore + sleepScore + activityScore;
        if(totalScore >= 25) {
            return "Good";
        } else if(totalScore >= 18) {
            return "Fair";
        } else {
            return "Poor";
        }
    }

    public int calculateStressScore(String stressLevel, String howOftenFeelTired, String fulfillment) {

        int stressScore = 0;

         switch (stressLevel) {
             case "Low":
                 stressScore += 5;
                 break;
             case "Moderate":
                 stressScore += 3;
                 break;
             case "High":
                 stressScore += 1;
                 break;
             default:
                 break;
        }

        switch (howOftenFeelTired) {
            case "Rarely":
                stressScore += 5;
                break;
            case "Sometimes":
                stressScore += 4;
                break;
            case "Often":
                stressScore += 2;
                break;
            case "Always":
                stressScore += 1;
                break;
            default:
                break;
        }

        switch (fulfillment) {
            case "Rarely":
                stressScore += 1;
                break;
            case "Sometimes":
                stressScore += 2;
                break;
            case "Often":
                stressScore += 4;
                break;
            case "Always":
                stressScore += 5;
                break;
            default:
                break;
        }

        return stressScore;
    }
    public int calculateSleepScore(int ratingOfSleep, int hoursOfSleep, String hardToSleep) {

        int sleepScore = 0;

        if (ratingOfSleep >= 8) {
            sleepScore += 5;
        } else if (ratingOfSleep >= 6) {
            sleepScore += 3;
        } else if (ratingOfSleep >= 4) {
            sleepScore += 1;
        } else {
            sleepScore += 0;
        }

        if (hoursOfSleep >= 7 && hoursOfSleep <= 9) {
            sleepScore += 5; // Optimal hours of sleep
        } else if (hoursOfSleep >= 5 && hoursOfSleep <= 11) {
            sleepScore += 2;
        } else {
            sleepScore += 0;
        }

        switch (hardToSleep) {
            case "Yes":
                sleepScore += 0;
                break;
            case "No":
                sleepScore += 3;
                break;
            default:
                break;
        }

        return sleepScore;

    }
    public int calculateActivityScore(String activePerWeek) {
        return switch (activePerWeek) {
            case "5+" -> 5; // Very active
            case "3-4" -> 4; // Moderately active
            case "1-2" -> 3; // Somewhat active
            case "None" -> 1; // Inactive
            default -> 0;
        };
    }

}

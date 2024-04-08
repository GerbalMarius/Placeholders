package com.placeholders.mindquest.startingquiz;

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

    @GetMapping("/starting-quiz")
    public String startingQuiz(Model model) {

        model.addAttribute("quizInfo", new StartingQuizInfo());
        return "starting-quiz";
    }

    @PostMapping("/starting-quiz/results")
    public String SubmitQuiz(@ModelAttribute StartingQuizInfo startingQuizInfo, Model model) {

        // Calculates mental state and prints info that is submitted
        String mentalState = calculateMentalState(startingQuizInfo);
        System.out.println("Submitted quiz info: " + startingQuizInfo);

        // Writes the submitted quiz info to a text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("quiz_info.txt", true))) {
            writer.write(startingQuizInfo.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Returns a view with the submitted data and mental state
        model.addAttribute("quizInfo", startingQuizInfo);
        model.addAttribute("mentalState", mentalState);
        return "starting-quiz-results";
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
             case "low":
                 stressScore += 5;
                 break;
             case "moderate":
                 stressScore += 3;
                 break;
             case "high":
                 stressScore += 1;
                 break;
             default:
                 break;
        }

        switch (howOftenFeelTired) {
            case "rarely":
                stressScore += 5;
                break;
            case "sometimes":
                stressScore += 4;
                break;
            case "often":
                stressScore += 2;
                break;
            case "always":
                stressScore += 1;
                break;
            default:
                break;
        }

        switch (fulfillment) {
            case "rarely":
                stressScore += 1;
                break;
            case "sometimes":
                stressScore += 2;
                break;
            case "often":
                stressScore += 4;
                break;
            case "always":
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
            case "yes":
                sleepScore += 0;
                break;
            case "no":
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
            case "none" -> 1; // Inactive
            default -> 0;
        };
    }

}

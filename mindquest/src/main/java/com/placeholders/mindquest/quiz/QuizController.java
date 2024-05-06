package com.placeholders.mindquest.quiz;

import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.points.Point;
import com.placeholders.mindquest.points.PointService;
import com.placeholders.mindquest.timestamp.TimeStamp;
import com.placeholders.mindquest.timestamp.TimeStampService;
import com.placeholders.mindquest.tips.Tip;
import com.placeholders.mindquest.tips.TipDao;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    private TipDao tipDao;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserService userService;


    @Autowired
    private TimeStampService timeStampService;

    @GetMapping("/create")
    public String getQuizCreationForm() {
        return "quiz-creation-form";
    }

    @PostMapping("/createQuiz")
    public String createQuiz(@RequestParam int numOfQuestions, @RequestParam String title) {
        quizService.createQuiz(numOfQuestions, title);
        return "quiz-created";
    }

    @GetMapping("/getQuiz")
    public String getRandomQuiz(Model model) {
        Quiz randomQuiz = quizService.getRandomQuiz();
        model.addAttribute("quiz", randomQuiz);
        return "quiz";
    }

    @PostMapping("/getScore")
    public String getScore(@RequestParam("quizId") Integer quizId, @RequestParam Map<String, String> allParams, Model model) {
        // Extracts responses from allParams
        ArrayList<Response> responses = new ArrayList<>();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("responses[")) {
                Response response = new Response();
                // Extracts the response ID from the key
                int questionId = Integer.parseInt(entry.getKey().replaceAll("[^\\d]", ""));
                response.setId(questionId);
                // Sets the response value from the selected radio button
                response.setResponse(entry.getValue());
                responses.add(response);
            }
        }

        int result = quizService.calculateResult(quizId, responses);
        int result1 = result*100/20;

        model.addAttribute("score", result1);

        saveUserPoints(result1);

        String category = getCategoryFromScore(result);

        Tip tip = tipDao.findRandomTipByCategory(category);

        if (tip != null) {
            model.addAttribute("tip", tip);
            saveTime(result1, tip.getContent());
        }

        return "quiz-result";
    }

    private String getCategoryFromScore(int score) {
        if (score >= 16) {
            return "high";
        } else if (score >= 11) {
            return "medium";
        } else {
            return "low";
        }
    }

    private  void saveUserPoints(final int result){
        Optional<User> currentUser = AuthController.currentUser();

        Point point = new Point(result);

        currentUser.ifPresent( user -> {
            final User realUser = userService.findUserByEmail(user.getEmail());
            point.setUser(realUser);
            realUser.addPoint(point);

            pointService.savePoint(point);
        });

    }

    private void saveTime(final int result, String content) {
        Optional<User> currentUser = AuthController.currentUser();

        TimeStamp timeStamp = new TimeStamp(LocalDateTime.now(), content, result);
        currentUser.ifPresent(user -> {
            final User realUser = userService.findUserByEmail(user.getEmail());

            timeStamp.setUser(realUser);
            realUser.addTimeStamp(timeStamp);
            timeStampService.save(timeStamp);
        });
    }

}

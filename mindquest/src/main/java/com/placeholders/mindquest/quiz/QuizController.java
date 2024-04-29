package com.placeholders.mindquest.quiz;

import com.placeholders.mindquest.tips.Tip;
import com.placeholders.mindquest.tips.TipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    private TipDao tipDao;

    @GetMapping("create")
    public String getQuizCreationForm() {
        return "get-quiz-creation-form";
    }

    @PostMapping("createQuiz")
    public String createQuiz(@RequestParam int numOfQuestions, @RequestParam String title) {
        quizService.createQuiz(numOfQuestions, title);
        return "create-quiz";
    }

    @GetMapping("getQuiz")
    public String getRandomQuiz(Model model) {
        Quiz randomQuiz = quizService.getRandomQuiz();
        model.addAttribute("quiz", randomQuiz);
        return "quiz";
    }

    @PostMapping("getScore")
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

        String category = getCategoryFromScore(result);

        Tip tip = tipDao.findRandomTipByCategory(category);

        if (tip != null) {
            model.addAttribute("tip", tip);
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

}

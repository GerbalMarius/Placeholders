package com.placeholders.mindquest.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public String createQuiz(int numOfQuestions, String title) {
        List<Question> questions = questionDao.findRandomQuestions(numOfQuestions);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return "success";
    }

    public Quiz getRandomQuiz() {
        List<Quiz> allQuizzes = quizDao.findAll();
        if(allQuizzes.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(allQuizzes.size());

        return allQuizzes.get(randomIndex);
    }

    public Integer calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).orElseThrow();

        System.out.println("Quiz title: " + quiz.getTitle());
        System.out.println("Quiz id: " + quiz.getId());
        for(Response response : responses) {
            System.out.println("Response id: " + response.getId());
            System.out.println("Response: " + response.getResponse());
        }


        List<Question> questions = quiz.getQuestions();

        int mentalWellnessScore = 0;
        for(Response response : responses) {

            Question question = findQuestionById(response.getId(), questions);

            mentalWellnessScore += getPointsForResponse(response, question);
        }

        return mentalWellnessScore;
    }

    private Question findQuestionById(Integer questionId, List<Question> questions) {
        for (Question question : questions) {
            if (question.getId().equals(questionId)) {
                return question;
            }
        }
        return null;
    }

    private int getPointsForResponse(Response response, Question question) {
        switch (response.getResponse()) {
            case "option1":
                return question.getOption1points();
            case "option2":
                return question.getOption2points();
            case "option3":
                return question.getOption3points();
            case "option4":
                return question.getOption4points();
            default:
                return 0;
        }
    }
}

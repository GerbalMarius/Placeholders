package com.placeholders.mindquest.starting_quiz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.placeholders.mindquest.login_utils.AuthController;
import com.placeholders.mindquest.quiz.Quiz;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import com.placeholders.mindquest.user_utils.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class StaringQuizControllerTests {

    @Mock
    private Model model;

    @Mock
    private StartingQuizRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;



    @InjectMocks
    private StaringQuizController controller;

    @BeforeEach
    public void setUp() {
        controller = new StaringQuizController(repository, userRepository);
    }

    @AfterEach
    public void tearDown() {
        controller = null;
    }

    @Test
    public void startingQuiz_ReturnsStartingQuizPage() {
        // Create an instance of the controller
        StaringQuizController controller = new StaringQuizController(repository, userRepository);

        // Create a mock Model object
        Model model = mock(Model.class);

        // Call the method being tested
        String viewName = controller.startingQuiz(model);

        // Assert that the method returns the expected view name
        assertEquals("starting-quiz", viewName);
    }

    private static User testUser(){
        return new User("test", "test", "test", "test");
    }

    @Test
    public void testSubmitQuiz() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();

        startingQuizInfo.setStressLevel("Low");
        startingQuizInfo.setHowOftenFeelTired("Rarely");
        startingQuizInfo.setFulfillment("Always");
        startingQuizInfo.setRatingOfSleep(9);
        startingQuizInfo.setHoursOfSleep(8);
        startingQuizInfo.setHardToSleep("No");
        startingQuizInfo.setActivePerWeek("5+");

        when(userService.findUserByEmail(any())).thenReturn(testUser());
        when(userRepository.findByEmail(any())).thenReturn(testUser());

        AuthController.setCurrentUser(userService.findUserByEmail(""));

        String viewName = controller.SubmitQuiz(startingQuizInfo, model);
        System.out.println(viewName);
        assertEquals("starting-quiz-result", viewName);
        verify(model).addAttribute(eq("quizInfo"), eq(startingQuizInfo));
        verify(model).addAttribute(eq("mentalState"), eq("Good"));
    }

    @Test
    public void testGetSetStartingQuizInfo() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();

        startingQuizInfo.setGender("female");
        startingQuizInfo.setHeight(1.72);
        startingQuizInfo.setWeight(55);

        assertEquals("female", startingQuizInfo.getGender());
        assertEquals(1.72, startingQuizInfo.getHeight());
        assertEquals(55, startingQuizInfo.getWeight());
    }

    @Test
    public void testCalculateMentalState1() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();
        startingQuizInfo.setStressLevel("Low");
        startingQuizInfo.setHowOftenFeelTired("Rarely");
        startingQuizInfo.setFulfillment("Always");
        startingQuizInfo.setRatingOfSleep(6);
        startingQuizInfo.setHoursOfSleep(7);
        startingQuizInfo.setHardToSleep("Yes");
        startingQuizInfo.setActivePerWeek("3-4");


        assertEquals("Good", controller.calculateMentalState(startingQuizInfo));
    }

    @Test
    public void testCalculateMentalState2() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();
        startingQuizInfo.setStressLevel("Moderate");
        startingQuizInfo.setHowOftenFeelTired("Sometimes");
        startingQuizInfo.setFulfillment("Sometimes");
        startingQuizInfo.setRatingOfSleep(7);
        startingQuizInfo.setHoursOfSleep(7);
        startingQuizInfo.setHardToSleep("Yes");
        startingQuizInfo.setActivePerWeek("1-2");

        assertEquals("Fair", controller.calculateMentalState(startingQuizInfo));
    }

    @Test
    public void testCalculateMentalState3() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();
        startingQuizInfo.setStressLevel("High");
        startingQuizInfo.setHowOftenFeelTired("Always");
        startingQuizInfo.setFulfillment("Rarely");
        startingQuizInfo.setRatingOfSleep(5);
        startingQuizInfo.setHoursOfSleep(4);
        startingQuizInfo.setHardToSleep("Yes");
        startingQuizInfo.setActivePerWeek("None");

        assertEquals("Poor", controller.calculateMentalState(startingQuizInfo));
    }

    @ParameterizedTest
    @CsvSource({
            "Low, Rarely, Always, 15",
            "Moderate, Sometimes, Often, 11",
            "High, Often, Rarely, 4",
            "High, Always, Sometimes, 4"
    })
    public void testCalculateStressScore(String stressLevel, String howOftenFeelTired, String fulfillment, int expected) {
        assertEquals(expected, controller.calculateStressScore(stressLevel, howOftenFeelTired, fulfillment));
    }

    @ParameterizedTest
    @CsvSource({
            "8, 8, No, 13",
            "6, 7, Yes, 8",
            "4, 5, No, 6",
            "3, 4, Yes, 0"
    })
    public void testCalculateSleepScore(int ratingOfSleep, int hoursOfSleep, String hardToSleep, int expected) {
        assertEquals(expected, controller.calculateSleepScore(ratingOfSleep, hoursOfSleep, hardToSleep));
    }

    @Test
    public void testCalculateActivityScore() {
        assertEquals(5, controller.calculateActivityScore("5+"));
        assertEquals(4, controller.calculateActivityScore("3-4"));
        assertEquals(3, controller.calculateActivityScore("1-2"));
        assertEquals(1, controller.calculateActivityScore("None"));
        assertEquals(0, controller.calculateActivityScore(""));
    }

}

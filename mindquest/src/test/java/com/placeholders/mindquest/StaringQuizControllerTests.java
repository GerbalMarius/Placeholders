package com.placeholders.mindquest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.placeholders.mindquest.startingquiz.StaringQuizController;
import com.placeholders.mindquest.startingquiz.StartingQuizInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class StaringQuizControllerTests {

    @Mock
    private Model model;

    private StaringQuizController controller;

    @BeforeEach
    public void setUp() {
        controller = new StaringQuizController();
    }

    @AfterEach
    public void tearDown() {
        controller = null;
    }

    @Test
    public void startingQuiz_ReturnsStartingQuizPage() {
        // Create an instance of the controller
        StaringQuizController controller = new StaringQuizController();

        // Create a mock Model object
        Model model = mock(Model.class);

        // Call the method being tested
        String viewName = controller.startingQuiz(model);

        // Assert that the method returns the expected view name
        assertEquals("starting-quiz", viewName);
    }

    @Test
    public void testSubmitQuiz() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();

        startingQuizInfo.setStressLevel("low");
        startingQuizInfo.setHowOftenFeelTired("rarely");
        startingQuizInfo.setFulfillment("always");
        startingQuizInfo.setRatingOfSleep(9);
        startingQuizInfo.setHoursOfSleep(8);
        startingQuizInfo.setHardToSleep("no");
        startingQuizInfo.setActivePerWeek("5+");

        String viewName = controller.SubmitQuiz(startingQuizInfo, model);
        assertEquals("starting-quiz-results", viewName);
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
        startingQuizInfo.setStressLevel("low");
        startingQuizInfo.setHowOftenFeelTired("rarely");
        startingQuizInfo.setFulfillment("always");
        startingQuizInfo.setRatingOfSleep(6);
        startingQuizInfo.setHoursOfSleep(7);
        startingQuizInfo.setHardToSleep("yes");
        startingQuizInfo.setActivePerWeek("3-4");

        String expected = "Good";

        assertEquals(expected, controller.calculateMentalState(startingQuizInfo));
    }

    @Test
    public void testCalculateMentalState2() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();
        startingQuizInfo.setStressLevel("moderate");
        startingQuizInfo.setHowOftenFeelTired("sometimes");
        startingQuizInfo.setFulfillment("sometimes");
        startingQuizInfo.setRatingOfSleep(7);
        startingQuizInfo.setHoursOfSleep(7);
        startingQuizInfo.setHardToSleep("yes");
        startingQuizInfo.setActivePerWeek("1-2");

        assertEquals("Fair", controller.calculateMentalState(startingQuizInfo));
    }

    @Test
    public void testCalculateMentalState3() {
        StartingQuizInfo startingQuizInfo = new StartingQuizInfo();
        startingQuizInfo.setStressLevel("high");
        startingQuizInfo.setHowOftenFeelTired("always");
        startingQuizInfo.setFulfillment("rarely");
        startingQuizInfo.setRatingOfSleep(5);
        startingQuizInfo.setHoursOfSleep(4);
        startingQuizInfo.setHardToSleep("yes");
        startingQuizInfo.setActivePerWeek("none");

        assertEquals("Poor", controller.calculateMentalState(startingQuizInfo));
    }

    @ParameterizedTest
    @CsvSource({
            "low, rarely, always, 15",
            "moderate, sometimes, often, 11",
            "high, often, rarely, 4",
            "high, always, sometimes, 4"
    })
    public void testCalculateStressScore(String stressLevel, String howOftenFeelTired, String fulfillment, int expected) {
        assertEquals(expected, controller.calculateStressScore(stressLevel, howOftenFeelTired, fulfillment));
    }

    @ParameterizedTest
    @CsvSource({
            "8, 8, no, 13",
            "6, 7, yes, 8",
            "4, 5, no, 6",
            "3, 4, yes, 0"
    })
    public void testCalculateSleepScore(int ratingOfSleep, int hoursOfSleep, String hardToSleep, int expected) {
        assertEquals(expected, controller.calculateSleepScore(ratingOfSleep, hoursOfSleep, hardToSleep));
    }

    @Test
    public void testCalculateActivityScore() {
        assertEquals(5, controller.calculateActivityScore("5+"));
        assertEquals(4, controller.calculateActivityScore("3-4"));
        assertEquals(3, controller.calculateActivityScore("1-2"));
        assertEquals(1, controller.calculateActivityScore("none"));
        assertEquals(0, controller.calculateActivityScore(""));
    }

}

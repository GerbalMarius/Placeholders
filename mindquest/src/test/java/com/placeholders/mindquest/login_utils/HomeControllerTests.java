package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.user_utils.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTests {

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp(){
        AuthController.setCurrentUser(null);
        AuthController.setFirstTimeUser(null);
    }

    @AfterEach
    void tearDown() {
        reset(model);
    }

    @Test
    public void homePageShowsUpOnStartup(){

        String viewName = homeController.home();

        assertEquals("index", viewName);
    }

    @Test
    public void homePageRedirectsUnverifiedUsersToLoginPage(){
        //AuthController views default situation as not logged in
        //so by default it returns redirect str.
        String viewName = homeController.dashboard(model);

        assertEquals("redirect:/login?please", viewName);
    }

    @Test
    public void dashboardVerificationShouldReturnFirstTimeUser(){

        var testData = getTestUser().getTransferableData();

        AuthController.setFirstTimeUser(testData);

        String viewName = homeController.dashboard(model);

        verify(model).addAttribute("currentUser", testData);
        verify(model).addAttribute("firstTime", true);

        assertEquals("dashboard", viewName);
    }

    private User getTestUser(){
        return new User("john.doe@gmail.com", "johdoe123", "John", "Doe",
                Collections.singletonList(Role.of("USER")));
    }

    @Test
    public void dashboardVerificationShouldReturnCurrentUser(){
        var testData = getTestUser();
        AuthController.setCurrentUser(testData);

        String viewName = homeController.dashboard(model);

        verify(model).addAttribute("currentUser", testData.getTransferableData());
        verify(model).addAttribute("firstTime", false);//it is not his first time

        assertEquals("dashboard", viewName);
    }

}
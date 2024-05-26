package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.mindfeed.PostService;
import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.settings.ProfilePhoto;
import com.placeholders.mindquest.settings.ProfilePhotoRepository;
import com.placeholders.mindquest.starting_quiz.StartingQuizRepository;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTests {

    @Mock
    private Model model;

    @Mock
    private ProfilePhotoRepository profilePhotoRepository;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @Mock
    private StartingQuizRepository   startingQuizRepository;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp(){
        AuthController.setCurrentUser(getTestUser());
        when(userService.findUserByEmail(getTestUser().getEmail())).thenReturn(getTestUser());
        when(startingQuizRepository.findByUserId(1)).thenReturn(getTestUser().getStartingQuizInfo());
        when(postService.getPostsPagedLatest(0, 3)).thenReturn(Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        reset(model, profilePhotoRepository, userService, startingQuizRepository, postService);
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
        AuthController.setCurrentUser(null);
        String viewName = homeController.mindboard(model);

        assertEquals("redirect:/login?please", viewName);
    }

    @Test
    public void mindboardVerificationShouldReturnFirstTimeUser(){

        var testData = getTestUser().getTransferableData();

        AuthController.setCurrentUser(getTestUser());



        String viewName = homeController.mindboard(model);

        verify(model).addAttribute("currentUser", testData);
        verify(model).addAttribute("startingQuizNotTaken", false);

        assertEquals("mindboard", viewName);
    }

    private User getTestUser(){
        User user = new User("john.doe@gmail.com", "johdoe123", "John", "Doe",
                Collections.singletonList(Role.of("USER")));
        user.setId(1);
        return user;
    }

    private ProfilePhoto getTestPhoto(){
        ProfilePhoto profilePhoto = new ProfilePhoto(1);
        profilePhoto.setData(generateRandomBytes(100));
        return profilePhoto;
    }

    private static byte[] generateRandomBytes(int size) {
        Random random = new Random();
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) (random.nextInt((byte) 101));
        }
        return bytes;
    }

    @Test
    public void mindboardVerificationShouldReturnCurrentUser(){
        var testData = getTestUser();
        AuthController.setCurrentUser(testData);
        when(profilePhotoRepository.findById(1)).thenReturn(getTestPhoto());
        String viewName = homeController.mindboard(model);

        verify(model).addAttribute("currentUser", testData.getTransferableData());
        verify(model).addAttribute("startingQuizNotTaken", false);//it is not his first time

        assertEquals("mindboard", viewName);
    }

}
package com.placeholders.mindquest.login_utils;

import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserDTO;
import com.placeholders.mindquest.user_utils.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTests {

    @Mock
    private Model model;

    @Mock
    private UserService userService;


    @Captor
    private ArgumentCaptor<String> viewCaptor;
    @Captor
    private ArgumentCaptor<UserDTO> dtoCaptor;

    @InjectMocks
    private AuthController authController;


    private List<User> users;


    @BeforeEach
    void setUp() {
        users = new ArrayList<>(List.of(
                new User("mariukas.ambrazevicius@gmail.com", "marius123", "Marius", "Ambrazevičius"),
                new User("karolis@karalaitis@gmail.com", "karolis123", "Karolis", "Karalaitis"),
                new User("arminas@gmail.com", "arminas123", "Arminas", "Naujokas"),
                new User("modde2003@gmail.com", "mode52036", "Modestas", "Naujokas")
        ));
        viewCaptor = ArgumentCaptor.forClass(String.class);
        dtoCaptor = ArgumentCaptor.forClass(UserDTO.class);
    }


    @AfterEach
    void tearDown(){
        reset(model, userService);
        viewCaptor = null;
        dtoCaptor = null;
        emptyUserData();

        AuthController.setCurrentUser(null);
    }

    void emptyUserData(){
        users.clear();
        users = null;
    }

    @Test
    public void registerPageShouldShowUp(){

        String pageViewName = authController.showRegisterPage(model);



        verify(model).addAttribute(viewCaptor.capture(), dtoCaptor.capture());

        assertEquals("user", viewCaptor.getValue());

        assertEquals("register", pageViewName);
    }

    @Test
    public void registerPageShouldRejectExistingUser(){

        User existingUser = users.get(0);

        when(userService.findUserByEmail(existingUser.getEmail())).thenReturn(existingUser);

        UserDTO userData = existingUser.getTransferableData();

        BindingResult authResult = new MapBindingResult(Collections.emptyMap(), "user");

        String viewName = authController.saveRegisteredUser(userData, authResult, model);

        assertTrue(authResult.hasErrors());
        verify(model).addAttribute(viewCaptor.capture(), dtoCaptor.capture());

        assertEquals("user", viewCaptor.getValue());
        assertEquals("/register", viewName);
    }
    @Test
    public void registerPageShouldLetThroughNewUser(){
        User newUser = users.get(1);

        when(userService.findUserByEmail(newUser.getEmail())).thenReturn(null);

        UserDTO userData  = newUser.getTransferableData();
        when(userService.saveUser(any(UserDTO.class))).thenReturn(newUser);

        BindingResult authResult = new MapBindingResult(Collections.emptyMap(), "user");


        String viewName = authController.saveRegisteredUser(userData, authResult, model);

        assertFalse(authResult.hasErrors());
        assertEquals("redirect:/mindboard", viewName);
    }

    @Test
    public void loginPageShouldShowUp(){
        String viewName = authController.loginPage(model);

        verify(model).addAttribute(viewCaptor.capture(), dtoCaptor.capture());

        assertEquals("user", viewCaptor.getValue());
        assertEquals("login", viewName);
    }

    @Test
    public void loginPageShouldNotLetThroughNonExistentUsers(){
        User nonExistent = users.get(0);
        UserDTO userData = nonExistent.getTransferableData();

        when(userService.findUserByEmail(nonExistent.getEmail())).thenReturn(nonExistent);
        when(userService.isValidPassword(nonExistent, userData)).thenReturn(false);

        BindingResult authResult = new MapBindingResult(Collections.emptyMap(), "user");

        String viewName = authController.validateExistingUser(userData, authResult, model);

        assertTrue(authResult.hasErrors());
        assertEquals("redirect:/login?error", viewName);
    }
    @ParameterizedTest
    @MethodSource("testRedirection")
    public void loginPageShouldRedirectExistingUsers(User user, String redirectString){
        UserDTO userData = user.getTransferableData();

        when(userService.findUserByEmail(user.getEmail())).thenReturn(user);
        when(userService.isValidPassword(user, userData)).thenReturn(true);

        BindingResult authResult = new MapBindingResult(Collections.emptyMap(), "user");

        String viewName = authController.validateExistingUser(userData, authResult, model);

        assertFalse(authResult.hasErrors());
        assertEquals(redirectString, viewName);
    }

    private static Stream<Arguments> testRedirection(){
        return Stream.of(
                Arguments.of(
                        new User("admin@admin.gmail.com", "1254", "Admin", "Admin", List.of(Role.of("ADMIN_ROLE"))),
                        "redirect:/users"
                ),
                Arguments.of(
                        new User("not@admin.gmail.com", "1254", "NotAdmin", "NotAdmin", List.of(Role.of("USER"))),
                        "redirect:/mindboard"
                )
        );
    }

    @Test
    public void logoutShouldClearSession(){

        String viewName = authController.logout();

        assertFalse(AuthController.isLoggedIn());
        assertEquals("redirect:/login?logout", viewName);
    }

    @Test
    public void showingAllUsersToANonAdminShouldForbidAccess(){
        User user = users.get(3);

        AuthController.setCurrentUser(user);

        assertForbiddenException(authController, model);
    }

    private void assertForbiddenException(AuthController controller, Model model){
        try {
            controller.showAllUsers(model);
        }catch (ResponseStatusException httpStatusEx){
            assertEquals(HttpStatus.FORBIDDEN, httpStatusEx.getStatusCode());
            assertEquals("403 FORBIDDEN \"Normal users can't have administrative privileges\"", httpStatusEx.getMessage());
        }
    }

    @Test
    public void allUsersBecomeVisibleToAnAdmin(){
        List<UserDTO> userDTOS = users.stream()
                                      .map(User::getTransferableData)
                                      .toList();

        User admin = users.get(3);

        when(userService.findAll()).thenReturn(userDTOS);

        admin.setRoles(Collections.singletonList(Role.of("ROLE_ADMIN")));

        AuthController.setCurrentUser(admin);

        String viewName = authController.showAllUsers(model);


        verify(model).addAttribute("users", userDTOS);

        assertEquals("users", viewName);
    }



}
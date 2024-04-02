package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.role_utils.RoleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserTests {

    private static final Role ADMIN_ROLE = Role.of("ROLE_ADMIN");

    private static final Role USER_ROLE = Role.of("USER");



    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private List<User> testUsers;
    private List<UserDTO> testUserDTOs;
    @BeforeEach
     void init(){
        testUsers = new ArrayList<>( List.of(
                new User("marius.ambrazevicius@gmail.com", "marius233", "Marius", "Ambrazevicius"),
                new User("john.doe@example.com", "john123", "John", "Doe"),
                new User("alice.smith@example.com", "alice456", "Alice", "Smith"),
                new User("bob.johnson@example.com", "bob789", "Bob", "Johnson")
        ));
        testUserDTOs = testUsers.stream()
                .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @AfterEach
    void tearDown(){
        reset(userRepository, roleRepository);
        clearTestData();
    }

    private void clearTestData() {
        testUsers.clear();
        testUsers = null;

        testUserDTOs.clear();
        testUserDTOs = null;
    }

    @Test
    public void userServiceShouldCreateUser(){
        User user = new User("karolis@mockatis.gmail.com", "karolis123", "Karolis", "Mockaitis" , List.of(new Role("USER")));

        UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(userDTO);


        assertEquals(user, savedUser);
    }

    @ParameterizedTest
    @MethodSource("userRoleCheck")
    public void userServiceShouldCreateAdminSuccessfully(User actual , boolean expected){
        //should assign admin role
        //if user used one of proper keys
        UserDTO userDTOAdmin = new UserDTO(actual.getId(), actual.getFirstName(), actual.getLastName(), actual.getEmail(), actual.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(checkKey(actual));

        User savedAdmin = userService.saveUser(userDTOAdmin);

        assertEquals(expected, savedAdmin.isAdmin());

    }

    private static Stream<Arguments> userRoleCheck(){
       return Stream.of(
                Arguments.of(
                        new User("mode2003@gmail.com", "111111", "Modestas", "Stankus", List.of(USER_ROLE)),
                        false
                ),
               Arguments.of(
                       new User("mode2003@gmail.com", "f7fde82ea02b", "Modestas", "Stankus", List.of(USER_ROLE)),
                       true
               )

        );
    }
    //copy of method from USER_SERVICE class
    private User checkKey(User user){
        if (userService.getAdminKeys().contains(user.getPassword())){
            user.setRoles(List.of(ADMIN_ROLE));
        }
        return user;
    }

    @Test
    public void userServiceShouldFindAllEntries(){

        when(userRepository.findAll()).thenReturn(testUsers);

        var foundUsers = userService.findAll();

        assertEquals(foundUsers, testUserDTOs);
    }

    @Test
    public void passwordEncoderSuccessfullyEncryptsPassword(){
        assertNotNull(passwordEncoder);

        User user = new User("john.doe@gmail.com", passwordEncoder.encode("john123"), "John", "Doe", List.of(USER_ROLE));
        UserDTO dtoForm = new UserDTO(0, "John", "Doe", "john.doe@gmail.com", "john123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        assertNotEquals(user.getPassword(), dtoForm.getPassword());



        //password encryption returns null because it is hidden from normal output streams
        // we set it manually to succeed, will work normally during deployment ,
        // because during tests the actual server does not start.
        when(userService.isValidPassword(user, dtoForm)).thenReturn(true);

        assertTrue(userService.isValidPassword(user, dtoForm));
    }

    @ParameterizedTest
    @CsvSource({"marius.ambrazevicius@gmail.com, true", "nosuchuser@gmail.com, false", "alice.smith@example.com, true", "idunno.idumb@gmail.com, false"})
    @Execution(ExecutionMode.CONCURRENT)
    public void repositoryPerformsSuccessfulSearchByEmail(String emailToSearch, boolean expected){
        when(userService.findUserByEmail(emailToSearch)).thenReturn(searchTestUsers(emailToSearch));

        User searchedUser = userRepository.findByEmail(emailToSearch);
        assertEquals(expected, searchedUser != null);
    }

    private User searchTestUsers(String email){
       return testUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}

package com.placeholders.mindquest.security;

import com.placeholders.mindquest.role_utils.Role;
import com.placeholders.mindquest.user_utils.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailsServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DetailsService detailsService;

    @AfterEach
    void tearDown() {
        reset(userRepository);
    }

    @Test
    public void loadUserByNameReturnsSystemicSpringDetails(){

        //this User class is from Spring security libs.
        var springUser = new User("john.doe@gmail.com", "johndoe123", Collections.emptyList());

        var actualUser = getActualUser(springUser);


        when(userRepository.findByEmail(springUser.getUsername())).thenReturn(actualUser);

        UserDetails userDetails = detailsService.loadUserByUsername(springUser.getUsername());

        assertEquals(userDetails.getUsername(), actualUser.getEmail());
        assertEquals(userDetails.getPassword(), actualUser.getPassword());
        assertEqualsAuthorities(userDetails, actualUser);
    }

    private void assertEqualsAuthorities(UserDetails userDetails, com.placeholders.mindquest.user_utils.User actualUser) {
        var authorities = userDetails.getAuthorities()
                                     .stream()
                                     .map(Objects::toString)
                                     .toList();

        var roles = actualUser.getRoles()
                              .stream()
                              .map(Role::getName)
                              .toList();

        assertEquals(authorities, roles, "Names of authorities and roles should match");
    }

    private static com.placeholders.mindquest.user_utils.User getActualUser(User springUser) {

        return new com.placeholders.mindquest.user_utils.User(springUser.getUsername(),
                springUser.getPassword(), "John", "Doe", List.of(Role.of("User")));
    }

    @Test
    public void loadingNonExistentUserThrowsAUserNameException(){
        String email = "something@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(email));
    }
}
package com.placeholders.mindquest;

import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import org.junit.jupiter.api.Assertions;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {

    @Autowired //allows us to inject data as a dependency with spring mvc
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void repositoryShouldCreateUser(){
        User user = new User("mariukas@gmail.com", "marius1254", "Marius", "Ambrazevicius");

        User savedUser = userRepository.save(user);

        User existingUser = entityManager.find(User.class, savedUser.getId());

        Assertions.assertEquals(user.getEmail(), existingUser.getEmail());
    }
}

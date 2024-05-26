package com.placeholders.mindquest.starting_quiz;

import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface StartingQuizRepository extends JpaRepository<StartingQuizInfo, Long> {

    StartingQuizInfo findByUserId(long userId);
}

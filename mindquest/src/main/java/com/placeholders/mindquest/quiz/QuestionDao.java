package com.placeholders.mindquest.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

    @Query(value = "SELECT * FROM question q ORDER BY RAND() LIMIT :numOfQuestions", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("numOfQuestions") int numOfQuestions);
}

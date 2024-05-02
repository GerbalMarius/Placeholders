package com.placeholders.mindquest.security;

import com.placeholders.mindquest.journals.JournalRepository;
import com.placeholders.mindquest.user_utils.User;
import com.placeholders.mindquest.user_utils.UserRepository;
import com.placeholders.mindquest.user_utils.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SqlService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Transactional
    public void deleteUser(String email) {
        try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
            userRepository.deleteByEmail(email);
        }catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
        finally {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
        }
    }

    @Transactional
    public void deleteJournal(int journalId) {
        try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");

            journalRepository.deleteById(journalId);
        }catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }finally {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
        }
    }
}
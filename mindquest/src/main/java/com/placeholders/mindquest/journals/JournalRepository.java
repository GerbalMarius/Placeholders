package com.placeholders.mindquest.journals;

import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal,Integer> {
    Page<Journal> findByUser(User user, Pageable pageable);

    long countByUser(User user);
}

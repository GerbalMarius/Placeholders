package com.placeholders.mindquest.requests;



import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    void deleteById(long id);

    List<Request> findAllByEmail(String email);
}
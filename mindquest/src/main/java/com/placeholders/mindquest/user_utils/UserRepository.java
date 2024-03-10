package com.placeholders.mindquest.user_utils;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A user repository that will allow us to perform user data CRUD (create, read, update , delete) operations without having to write code ourselves.
 * Methods will be automatically generated by Spring MVC, no need for manual implementation.
 * @author marius
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

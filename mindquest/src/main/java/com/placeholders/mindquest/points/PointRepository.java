package com.placeholders.mindquest.points;

import com.placeholders.mindquest.user_utils.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findPointsByUser(User user);
}
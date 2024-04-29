package com.placeholders.mindquest.tips;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TipDao extends JpaRepository<Tip,Integer> {
    @Query(value = "SELECT * FROM tips WHERE category = :category ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Tip findRandomTipByCategory(@Param("category") String category);
}

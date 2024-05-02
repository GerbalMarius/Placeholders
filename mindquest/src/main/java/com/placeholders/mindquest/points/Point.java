package com.placeholders.mindquest.points;

import com.placeholders.mindquest.user_utils.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Represents the point value given by taking a single quiz
 */

@Entity
@Table(name = "points")
@Data
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "total_points")
    private int value;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Point() {}

    public Point(int value) {
        this.value = value;
    }
    public Point(int value, User user) {
        this.value = value;
        this.user = user;
    }
}

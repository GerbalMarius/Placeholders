package com.placeholders.mindquest.starting_quiz;

import com.placeholders.mindquest.user_utils.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "starter_quizzes")
public class StartingQuizInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "gender", length = 40)
    private String gender;

    @Column(name = "height", precision = 4)
    private double height;

    @Column(name = "weight", precision = 4)
    private double weight;

    @Column(name = "sleep_rating")
    private int ratingOfSleep;

    @Column(name = "sleep_hours")
    private int hoursOfSleep;

    @Column(name = "hard_sleep", length = 70)
    private String hardToSleep;

    @Column(name = "tiredness", length = 70)
    private String howOftenFeelTired;

    @Column(name = "stress", length = 70)
    private String stressLevel;

    @Column(name = "active", length = 70)
    private String activePerWeek;

    @Column(name = "fullfilment", length = 70)
    private String fulfillment;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Override
    public String toString() {
        return  gender + ";" + height + ";" + weight + ";" + ratingOfSleep + ";" + hoursOfSleep + ";" + hardToSleep + ";" +
                howOftenFeelTired + ";" + stressLevel + ";" + activePerWeek + ";" + fulfillment;
    }
}

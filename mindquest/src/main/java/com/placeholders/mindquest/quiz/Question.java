package com.placeholders.mindquest.quiz;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the question
    private String questionTitle; // The text of the question
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int option1points;
    private int option2points;
    private int option3points;
    private int option4points;

    public Question() {
        // Default constructor
    }

    public Question(Integer id, String questionTitle, String option1, String option2, String option3, String option4, int option1points, int option2points, int option3points, int option4points) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option1points = option1points;
        this.option2points = option2points;
        this.option3points = option3points;
        this.option4points = option4points;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionTitle='" + questionTitle + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", option1points=" + option1points +
                ", option2points=" + option2points +
                ", option3points=" + option3points +
                ", option4points=" + option4points +
                '}';
    }
}

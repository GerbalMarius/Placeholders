package com.placeholders.mindquest.quiz;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    // Getter and setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getOption1points() {
        return option1points;
    }

    public void setOption1points(int option1points) {
        this.option1points = option1points;
    }

    public int getOption2points() {
        return option2points;
    }

    public void setOption2points(int option2points) {
        this.option2points = option2points;
    }

    public int getOption3points() {
        return option3points;
    }

    public void setOption3points(int option3points) {
        this.option3points = option3points;
    }

    public int getOption4points() {
        return option4points;
    }

    public void setOption4points(int option4points) {
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

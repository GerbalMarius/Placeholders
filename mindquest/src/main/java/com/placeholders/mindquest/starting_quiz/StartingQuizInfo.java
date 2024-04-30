package com.placeholders.mindquest.starting_quiz;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StartingQuizInfo {
    private String gender;

    private double height;

    private double weight;

    private int ratingOfSleep;

    private int hoursOfSleep;

    private String hardToSleep;

    private String howOftenFeelTired;

    private String stressLevel;

    private String activePerWeek;

    private String fulfillment;

    @Override
    public String toString() {
        return  gender + ";" + height + ";" + weight + ";" + ratingOfSleep + ";" + hoursOfSleep + ";" + hardToSleep + ";" +
                howOftenFeelTired + ";" + stressLevel + ";" + activePerWeek + ";" + fulfillment;
    }
}

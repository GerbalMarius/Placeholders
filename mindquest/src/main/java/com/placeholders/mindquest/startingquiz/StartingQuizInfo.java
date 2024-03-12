package com.placeholders.mindquest.startingquiz;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getRatingOfSleep() {
        return ratingOfSleep;
    }

    public void setRatingOfSleep(int ratingOfSleep) {
        this.ratingOfSleep = ratingOfSleep;
    }

    public int getHoursOfSleep() {
        return hoursOfSleep;
    }

    public void setHoursOfSleep(int hoursOfSleep) {
        this.hoursOfSleep = hoursOfSleep;
    }

    public String getHardToSleep() {
        return hardToSleep;
    }

    public void setHardToSleep(String hardToSleep) {
        this.hardToSleep = hardToSleep;
    }

    public String getHowOftenFeelTired() {
        return howOftenFeelTired;
    }

    public void setHowOftenFeelTired(String howOftenFeelTired) {
        this.howOftenFeelTired = howOftenFeelTired;
    }

    public String getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(String stressLevel) {
        this.stressLevel = stressLevel;
    }

    public String getActivePerWeek() {
        return activePerWeek;
    }

    public void setActivePerWeek(String activePerWeek) {
        this.activePerWeek = activePerWeek;
    }

    public String getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(String fulfillment) {
        this.fulfillment = fulfillment;
    }

    @Override
    public String toString() {
        return  gender + ";" + height + ";" + weight + ";" + ratingOfSleep + ";" + hoursOfSleep + ";" + hardToSleep + ";" +
                howOftenFeelTired + ";" + stressLevel + ";" + activePerWeek + ";" + fulfillment;
    }
}

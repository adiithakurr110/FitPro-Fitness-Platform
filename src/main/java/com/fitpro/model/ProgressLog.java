package com.fitpro.model;

import java.time.LocalDate;

/** One logged training session (what the member actually did). */
public class ProgressLog {
    private int id;
    private int userId;
    private int workoutId;
    private String workoutTitle;
    private int minutes;
    private double weightKg;
    private double calories;
    private LocalDate loggedOn;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getWorkoutId() { return workoutId; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    public String getWorkoutTitle() { return workoutTitle; }
    public void setWorkoutTitle(String workoutTitle) { this.workoutTitle = workoutTitle; }
    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }
    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public LocalDate getLoggedOn() { return loggedOn; }
    public void setLoggedOn(LocalDate loggedOn) { this.loggedOn = loggedOn; }
}

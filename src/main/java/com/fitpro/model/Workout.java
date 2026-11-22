package com.fitpro.model;

/**
 * Abstract workout program. Concrete types (Cardio, Strength, Yoga) supply their own
 * energy expenditure formula, so callers use {@code workout.caloriesBurned(...)} -
 * runtime POLYMORPHISM through the {@link CalorieCalculable} interface.
 */
public abstract class Workout implements CalorieCalculable {
    private int id;
    private String title;
    private String description;
    private String level;
    private int durationMin;
    private double metValue;
    private int trainerId;
    private String trainerName;

    /** Workout category as stored in the DB: CARDIO, STRENGTH or YOGA. */
    public abstract String getType();

    /**
     * Standard ACSM Metabolic Equivalent of Task (MET) formula:
     * kcal = MET * 3.5 * weight(kg) / 200 * minutes.
     * Handles non-positive input values safely by returning 0.0.
     */
    protected double baseCalories(double weightKg, int minutes) {
        if (weightKg <= 0 || minutes <= 0 || metValue <= 0) {
            return 0.0;
        }
        return metValue * 3.5 * weightKg / 200.0 * minutes;
    }

    /** Factory that returns the correct subclass for a DB type string. */
    public static Workout of(String type) {
        switch (type == null ? "" : type.toUpperCase()) {
            case "STRENGTH": return new StrengthWorkout();
            case "YOGA":     return new YogaWorkout();
            default:         return new CardioWorkout();
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public int getDurationMin() { return durationMin; }
    public void setDurationMin(int durationMin) { this.durationMin = durationMin; }
    public double getMetValue() { return metValue; }
    public void setMetValue(double metValue) { this.metValue = metValue; }
    public int getTrainerId() { return trainerId; }
    public void setTrainerId(int trainerId) { this.trainerId = trainerId; }
    public String getTrainerName() { return trainerName == null ? "FitPro Team" : trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
}

package com.fitpro.model;

/** Cardio sessions use the plain MET formula. */
public class CardioWorkout extends Workout {
    @Override public String getType() { return "CARDIO"; }

    @Override
    public double caloriesBurned(double weightKg, int minutes) {
        return baseCalories(weightKg, minutes);
    }
}

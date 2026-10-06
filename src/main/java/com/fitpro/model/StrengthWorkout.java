package com.fitpro.model;

/** Strength sessions add a 10% "afterburn" (EPOC) bonus. */
public class StrengthWorkout extends Workout {
    private static final double AFTERBURN = 1.10;

    @Override public String getType() { return "STRENGTH"; }

    @Override
    public double caloriesBurned(double weightKg, int minutes) {
        return baseCalories(weightKg, minutes) * AFTERBURN;
    }
}

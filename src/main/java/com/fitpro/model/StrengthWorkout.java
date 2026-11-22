package com.fitpro.model;

/**
 * Strength and hypertrophy workout.
 * Applies a 10% afterburn (Excess Post-Exercise Oxygen Consumption - EPOC) factor.
 * Note: Multiplier is an educational estimate for academic fitness modeling.
 */
public class StrengthWorkout extends Workout {
    private static final double AFTERBURN = 1.10;

    @Override
    public String getType() {
        return "STRENGTH";
    }

    @Override
    public double caloriesBurned(double weightKg, int minutes) {
        return baseCalories(weightKg, minutes) * AFTERBURN;
    }
}

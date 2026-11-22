package com.fitpro.model;

/**
 * Yoga and mindful mobility workout.
 * Applies a 0.90 low-intensity adjustment factor.
 * Note: Factor is an educational estimate for academic fitness modeling.
 */
public class YogaWorkout extends Workout {
    private static final double LOW_INTENSITY = 0.90;

    @Override
    public String getType() {
        return "YOGA";
    }

    @Override
    public double caloriesBurned(double weightKg, int minutes) {
        return baseCalories(weightKg, minutes) * LOW_INTENSITY;
    }
}

package com.fitpro.model;

/** Yoga is low intensity, so the estimate is reduced slightly. */
public class YogaWorkout extends Workout {
    private static final double LOW_INTENSITY = 0.90;

    @Override public String getType() { return "YOGA"; }

    @Override
    public double caloriesBurned(double weightKg, int minutes) {
        return baseCalories(weightKg, minutes) * LOW_INTENSITY;
    }
}

package com.fitpro.model;

/** Contract for anything that can estimate calories burned. */
public interface CalorieCalculable {
    /**
     * @param weightKg body weight of the person in kilograms
     * @param minutes  duration of the session
     * @return estimated calories burned
     */
    double caloriesBurned(double weightKg, int minutes);
}

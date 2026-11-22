package com.fitpro.service;

import com.fitpro.dao.ProgressDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.ProgressLog;
import com.fitpro.model.Workout;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for session logging and member performance analytics.
 * Demonstrates: Java Collections (List, Map, TreeMap), Streams, Lambdas, and Aggregations.
 */
public class ProgressService {
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    /** Immutable summary data transfer object handed to the JSP view. */
    public static class Summary {
        private final int sessions;
        private final int totalMinutes;
        private final double totalCalories;
        private final double latestWeight;
        private final String favouriteWorkout;
        private final Map<String, Double> caloriesByWorkout;

        Summary(int sessions, int totalMinutes, double totalCalories, double latestWeight,
                String favouriteWorkout, Map<String, Double> caloriesByWorkout) {
            this.sessions = sessions;
            this.totalMinutes = totalMinutes;
            this.totalCalories = totalCalories;
            this.latestWeight = latestWeight;
            this.favouriteWorkout = favouriteWorkout;
            this.caloriesByWorkout = caloriesByWorkout;
        }
        public int getSessions() { return sessions; }
        public int getTotalMinutes() { return totalMinutes; }
        public double getTotalCalories() { return totalCalories; }
        public double getLatestWeight() { return latestWeight; }
        public String getFavouriteWorkout() { return favouriteWorkout; }
        public Map<String, Double> getCaloriesByWorkout() { return caloriesByWorkout; }
    }

    /**
     * Records a member workout session. Calories are computed polymorphically by the
     * workout subclass (Cardio / Strength / Yoga).
     */
    public ProgressLog logSession(int userId, int workoutId, String minutesText, String weightText) throws AppException {
        int minutes;
        double weight;
        try {
            minutes = Integer.parseInt(minutesText.trim());
            weight = Double.parseDouble(weightText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new AppException("Minutes and weight must be valid positive numbers.");
        }
        if (minutes < 1 || minutes > 360) throw new AppException("Duration must be between 1 and 360 minutes.");
        if (weight < 20.0 || weight > 300.0) throw new AppException("Weight must be between 20 and 300 kg.");

        Workout workout = workoutDAO.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout program not found."));

        ProgressLog log = new ProgressLog();
        log.setUserId(userId);
        log.setWorkoutId(workoutId);
        log.setMinutes(minutes);
        log.setWeightKg(weight);
        log.setCalories(Math.round(workout.caloriesBurned(weight, minutes) * 10) / 10.0);
        log.setLoggedOn(LocalDate.now());
        progressDAO.add(log);
        return log;
    }

    public List<ProgressLog> history(int userId) throws AppException {
        return progressDAO.findByUser(userId);
    }

    /** Aggregates all logs of a member into dashboard metrics using Java 8 Streams. */
    public Summary summarize(int userId) throws AppException {
        List<ProgressLog> logs = progressDAO.findByUser(userId);

        int minutes = logs.stream().mapToInt(ProgressLog::getMinutes).sum();
        double calories = logs.stream().mapToDouble(ProgressLog::getCalories).sum();
        double latestWeight = logs.isEmpty() ? 0 : logs.get(0).getWeightKg();

        // TreeMap keeps workout names in alphabetical order for consistent chart rendering
        Map<String, Double> byWorkout = logs.stream().collect(Collectors.groupingBy(
                ProgressLog::getWorkoutTitle, TreeMap::new, Collectors.summingDouble(ProgressLog::getCalories)));

        // Most frequently trained routine based on session counts
        String favourite = logs.stream()
                .collect(Collectors.groupingBy(ProgressLog::getWorkoutTitle, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        return new Summary(logs.size(), minutes, Math.round(calories * 10) / 10.0,
                latestWeight, favourite, byWorkout);
    }
}

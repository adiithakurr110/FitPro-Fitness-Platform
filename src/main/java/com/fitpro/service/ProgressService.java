package com.fitpro.service;

import com.fitpro.dao.ProgressDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.ProgressLog;
import com.fitpro.model.Workout;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/** Logs sessions and builds the dashboard statistics (uses COLLECTIONS + generics + streams). */
public class ProgressService {
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    /** Immutable summary object handed to the JSP. */
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
     * Records a session. Calories are computed by the workout object itself,
     * so each type (cardio / strength / yoga) applies its own formula.
     */
    public ProgressLog logSession(int userId, int workoutId, String minutesText, String weightText) throws AppException {
        int minutes;
        double weight;
        try {
            minutes = Integer.parseInt(minutesText.trim());
            weight = Double.parseDouble(weightText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new AppException("Minutes and weight must be numbers.");
        }
        if (minutes < 1 || minutes > 600) throw new AppException("Minutes must be between 1 and 600.");
        if (weight < 20 || weight > 300) throw new AppException("Weight must be between 20 and 300 kg.");

        Workout workout = workoutDAO.findById(workoutId)
                .orElseThrow(() -> new AppException("Workout not found."));

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

    /** Aggregates all logs of a member into dashboard numbers. */
    public Summary summarize(int userId) throws AppException {
        List<ProgressLog> logs = progressDAO.findByUser(userId);   // newest first

        int minutes = logs.stream().mapToInt(ProgressLog::getMinutes).sum();
        double calories = logs.stream().mapToDouble(ProgressLog::getCalories).sum();
        double latestWeight = logs.isEmpty() ? 0 : logs.get(0).getWeightKg();

        // TreeMap keeps workout names alphabetical for display
        Map<String, Double> byWorkout = logs.stream().collect(Collectors.groupingBy(
                ProgressLog::getWorkoutTitle, TreeMap::new, Collectors.summingDouble(ProgressLog::getCalories)));

        // most-trained workout = highest session count
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

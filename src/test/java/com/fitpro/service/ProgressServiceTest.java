package com.fitpro.service;

import com.fitpro.exception.AppException;
import com.fitpro.model.ProgressLog;
import com.fitpro.model.User;
import com.fitpro.model.Workout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class ProgressServiceTest {

    private final ProgressService progressService = new ProgressService();
    private final WorkoutService workoutService = new WorkoutService();
    private final AuthService authService = new AuthService();

    @Test
    @DisplayName("Log session computes calories and preserves session data")
    public void testLogSessionSuccess() throws Exception {
        String memberEmail = "progress_user_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User member = authService.register("Session Logger", memberEmail, "logger123456", "MEMBER");

        List<Workout> workouts = workoutService.list("CARDIO");
        Assertions.assertFalse(workouts.isEmpty());
        Workout w = workouts.get(0);

        ProgressLog log = progressService.logSession(member.getId(), w.getId(), "45", "75.0");
        Assertions.assertNotNull(log);
        Assertions.assertEquals(member.getId(), log.getUserId());
        Assertions.assertEquals(w.getId(), log.getWorkoutId());
        Assertions.assertEquals(45, log.getMinutes());
        Assertions.assertEquals(75.0, log.getWeightKg());
        Assertions.assertTrue(log.getCalories() > 0);

        List<ProgressLog> history = progressService.history(member.getId());
        Assertions.assertFalse(history.isEmpty());
        Assertions.assertEquals(log.getCalories(), history.get(0).getCalories(), 0.01);
    }

    @Test
    @DisplayName("Log session rejects non-numeric or out-of-range inputs")
    public void testLogSessionValidation() throws Exception {
        String memberEmail = "val_prog_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User member = authService.register("Prog Val", memberEmail, "prog123456", "MEMBER");

        List<Workout> workouts = workoutService.list(null);
        int workoutId = workouts.get(0).getId();

        // Non-numeric minutes
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), workoutId, "abc", "70.0"));

        // Non-numeric weight
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), workoutId, "30", "xyz"));

        // Out of range minutes
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), workoutId, "0", "70.0"));
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), workoutId, "1000", "70.0"));

        // Out of range weight
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), workoutId, "30", "10.0"));
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), workoutId, "30", "400.0"));

        // Nonexistent workout ID
        Assertions.assertThrows(AppException.class, () ->
                progressService.logSession(member.getId(), 999999, "30", "70.0"));
    }

    @Test
    @DisplayName("Summary correctly aggregates session count, total minutes, calories, and favorite workout")
    public void testSummarizeCalculation() throws Exception {
        String memberEmail = "summary_user_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User member = authService.register("Summary User", memberEmail, "sum123456", "MEMBER");

        // Initially zero
        ProgressService.Summary initialSummary = progressService.summarize(member.getId());
        Assertions.assertEquals(0, initialSummary.getSessions());
        Assertions.assertEquals(0, initialSummary.getTotalMinutes());
        Assertions.assertEquals(0.0, initialSummary.getTotalCalories());
        Assertions.assertEquals(0.0, initialSummary.getLatestWeight());
        Assertions.assertEquals("-", initialSummary.getFavouriteWorkout());

        List<Workout> workouts = workoutService.list(null);
        Workout w1 = workouts.get(0);

        // Log 2 sessions for w1
        progressService.logSession(member.getId(), w1.getId(), "30", "80.0");
        progressService.logSession(member.getId(), w1.getId(), "20", "79.5");

        ProgressService.Summary updatedSummary = progressService.summarize(member.getId());
        Assertions.assertEquals(2, updatedSummary.getSessions());
        Assertions.assertEquals(50, updatedSummary.getTotalMinutes());
        Assertions.assertEquals(79.5, updatedSummary.getLatestWeight(), 0.01);
        Assertions.assertTrue(updatedSummary.getTotalCalories() > 0);
        Assertions.assertEquals(w1.getTitle(), updatedSummary.getFavouriteWorkout());
        Assertions.assertTrue(updatedSummary.getCaloriesByWorkout().containsKey(w1.getTitle()));
    }
}

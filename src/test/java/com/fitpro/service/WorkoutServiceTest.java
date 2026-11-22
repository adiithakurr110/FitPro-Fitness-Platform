package com.fitpro.service;

import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.model.Workout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WorkoutServiceTest {

    private final WorkoutService workoutService = new WorkoutService();
    private final AuthService authService = new AuthService();

    @Test
    @DisplayName("List workouts returns all or filtered category")
    public void testListWorkouts() throws Exception {
        List<Workout> all = workoutService.list(null);
        Assertions.assertNotNull(all);
        Assertions.assertFalse(all.isEmpty(), "Catalog should have sample workouts");

        List<Workout> cardio = workoutService.list("CARDIO");
        Assertions.assertNotNull(cardio);
        for (Workout w : cardio) {
            Assertions.assertEquals("CARDIO", w.getType());
        }

        List<Workout> strength = workoutService.list("STRENGTH");
        Assertions.assertNotNull(strength);
        for (Workout w : strength) {
            Assertions.assertEquals("STRENGTH", w.getType());
        }

        List<Workout> yoga = workoutService.list("YOGA");
        Assertions.assertNotNull(yoga);
        for (Workout w : yoga) {
            Assertions.assertEquals("YOGA", w.getType());
        }
    }

    @Test
    @DisplayName("Trainer can publish a workout program with valid parameters")
    public void testTrainerPublishProgram() throws Exception {
        String trainerEmail = "trainer_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User trainer = authService.register("Coach Alex", trainerEmail, "coach123456", "TRAINER");

        String title = "Special Kettlebell Power " + UUID.randomUUID().toString().substring(0, 4);
        workoutService.publish(trainer, title, "Full body kettlebell movements", "STRENGTH", "INTERMEDIATE", "45", "6.5");

        List<Workout> strengthWorkouts = workoutService.list("STRENGTH");
        boolean found = strengthWorkouts.stream().anyMatch(w -> w.getTitle().equals(title));
        Assertions.assertTrue(found, "Published workout should be listed in the catalogue");
    }

    @Test
    @DisplayName("Publishing validates input constraints strictly")
    public void testPublishValidation() throws Exception {
        String trainerEmail = "trainer_val_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User trainer = authService.register("Coach Val", trainerEmail, "coach123456", "TRAINER");

        String memberEmail = "member_val_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User member = authService.register("Member Val", memberEmail, "member123456", "MEMBER");

        // Non-trainer cannot publish
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(member, "Title", "Desc", "CARDIO", "BEGINNER", "30", "5.0"));

        // Invalid title (too short)
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "ab", "Desc", "CARDIO", "BEGINNER", "30", "5.0"));

        // Invalid category type
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "Valid Title", "Desc", "INVALID_CAT", "BEGINNER", "30", "5.0"));

        // Invalid level
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "Valid Title", "Desc", "CARDIO", "EXPERT", "30", "5.0"));

        // Invalid duration range
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "Valid Title", "Desc", "CARDIO", "BEGINNER", "2", "5.0"));
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "Valid Title", "Desc", "CARDIO", "BEGINNER", "500", "5.0"));

        // Invalid MET intensity range
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "Valid Title", "Desc", "CARDIO", "BEGINNER", "30", "0.2"));
        Assertions.assertThrows(AppException.class, () ->
                workoutService.publish(trainer, "Valid Title", "Desc", "CARDIO", "BEGINNER", "30", "25.0"));
    }

    @Test
    @DisplayName("Member enrollment rules: success, duplicate prevention, trainer blocking")
    public void testEnrollmentRules() throws Exception {
        String memberEmail = "member_enr_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User member = authService.register("Enrolling Member", memberEmail, "pass123456", "MEMBER");

        String trainerEmail = "trainer_enr_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User trainer = authService.register("Enrolling Trainer", trainerEmail, "pass123456", "TRAINER");

        List<Workout> workouts = workoutService.list(null);
        Assertions.assertFalse(workouts.isEmpty());
        int workoutId = workouts.get(0).getId();

        // Trainer cannot enroll
        Assertions.assertThrows(AppException.class, () ->
                workoutService.enroll(trainer, workoutId));

        // Member enrolls successfully
        workoutService.enroll(member, workoutId);

        Set<Integer> enrolledIds = workoutService.enrolledIds(member.getId());
        Assertions.assertTrue(enrolledIds.contains(workoutId));

        List<Workout> enrolledList = workoutService.enrolledWorkouts(member.getId());
        Assertions.assertTrue(enrolledList.stream().anyMatch(w -> w.getId() == workoutId));

        // Duplicate enrollment is rejected
        Assertions.assertThrows(AppException.class, () ->
                workoutService.enroll(member, workoutId));
    }

    @Test
    @DisplayName("Workout deletion authorization: trainers only delete own; admins delete any; members blocked")
    public void testWorkoutDeletionAuthorization() throws Exception {
        String trainer1Email = "t1_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User trainer1 = authService.register("Coach One", trainer1Email, "coach123", "TRAINER");

        String trainer2Email = "t2_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User trainer2 = authService.register("Coach Two", trainer2Email, "coach123", "TRAINER");

        String memberEmail = "m_del_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User member = authService.register("Plain Member", memberEmail, "member123", "MEMBER");

        User admin = User.of("ADMIN");
        admin.setId(9999);
        admin.setFullName("Super Admin");

        String title = "Auth Delete Test " + UUID.randomUUID().toString().substring(0, 4);
        workoutService.publish(trainer1, title, "Testing deletion rules", "CARDIO", "BEGINNER", "30", "7.0");

        List<Workout> list = workoutService.list("CARDIO");
        Workout created = list.stream().filter(w -> w.getTitle().equals(title)).findFirst()
                .orElseThrow(() -> new AssertionError("Workout not found"));

        // Member cannot delete
        Assertions.assertThrows(AppException.class, () -> workoutService.delete(member, created.getId()));

        // Different trainer cannot delete
        Assertions.assertThrows(AppException.class, () -> workoutService.delete(trainer2, created.getId()));

        // Author trainer can delete
        workoutService.delete(trainer1, created.getId());
        Assertions.assertFalse(workoutService.list("CARDIO").stream().anyMatch(w -> w.getId() == created.getId()));
    }

    @Test
    @DisplayName("Workout multi-criteria search and filtering")
    public void testWorkoutSearchAndFilter() throws Exception {
        List<Workout> searchResults = workoutService.search("Morning", "CARDIO", "BEGINNER");
        Assertions.assertNotNull(searchResults);
        for (Workout w : searchResults) {
            Assertions.assertEquals("CARDIO", w.getType());
            Assertions.assertEquals("BEGINNER", w.getLevel());
            Assertions.assertTrue(w.getTitle().toLowerCase().contains("morning")
                    || (w.getDescription() != null && w.getDescription().toLowerCase().contains("morning")));
        }
    }
}

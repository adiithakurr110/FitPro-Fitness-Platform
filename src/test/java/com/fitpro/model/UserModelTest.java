package com.fitpro.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserModelTest {

    @Test
    @DisplayName("User factory instantiates Member, Trainer, and Admin polymorphically")
    public void testUserFactory() {
        User member = User.of("MEMBER");
        User trainer = User.of("TRAINER");
        User admin = User.of("ADMIN");
        User defaultUser = User.of("OTHER");

        Assertions.assertTrue(member instanceof Member);
        Assertions.assertEquals("MEMBER", member.getRole());
        Assertions.assertFalse(member.isTrainer());
        Assertions.assertFalse(member.isAdmin());
        Assertions.assertTrue(member.isMember());

        Assertions.assertTrue(trainer instanceof Trainer);
        Assertions.assertEquals("TRAINER", trainer.getRole());
        Assertions.assertTrue(trainer.isTrainer());
        Assertions.assertFalse(trainer.isAdmin());

        Assertions.assertTrue(admin instanceof Admin);
        Assertions.assertEquals("ADMIN", admin.getRole());
        Assertions.assertTrue(admin.isAdmin());
        Assertions.assertFalse(admin.isTrainer());
        Assertions.assertFalse(admin.isMember());

        Assertions.assertTrue(defaultUser instanceof Member);
    }

    @Test
    @DisplayName("Welcome messages differ according to role polymorphism")
    public void testWelcomeMessagePolymorphism() {
        User member = User.of("MEMBER");
        member.setFullName("John Doe");

        User trainer = User.of("TRAINER");
        trainer.setFullName("Sarah Connor");

        String memberMsg = member.getWelcomeMessage();
        String trainerMsg = trainer.getWelcomeMessage();

        Assertions.assertTrue(memberMsg.contains("John Doe"));
        Assertions.assertTrue(trainerMsg.contains("Sarah Connor"));
        Assertions.assertTrue(trainerMsg.toLowerCase().contains("coach"));
        Assertions.assertNotEquals(memberMsg, trainerMsg);
    }

    @Test
    @DisplayName("ProgressLog and EnrollmentDetail models hold attributes faithfully")
    public void testProgressAndEnrollmentModels() {
        ProgressLog log = new ProgressLog();
        log.setId(10);
        log.setUserId(2);
        log.setWorkoutId(3);
        log.setWorkoutTitle("Sprint Training");
        log.setMinutes(45);
        log.setWeightKg(72.5);
        log.setCalories(450.0);
        log.setLoggedOn(java.time.LocalDate.of(2026, 11, 12));

        Assertions.assertEquals(10, log.getId());
        Assertions.assertEquals(2, log.getUserId());
        Assertions.assertEquals(3, log.getWorkoutId());
        Assertions.assertEquals("Sprint Training", log.getWorkoutTitle());
        Assertions.assertEquals(45, log.getMinutes());
        Assertions.assertEquals(72.5, log.getWeightKg());
        Assertions.assertEquals(450.0, log.getCalories());
        Assertions.assertEquals(java.time.LocalDate.of(2026, 11, 12), log.getLoggedOn());

        EnrollmentDetail detail = new EnrollmentDetail();
        detail.setId(1);
        detail.setUserId(2);
        detail.setUserName("Alice");
        detail.setUserEmail("alice@example.com");
        detail.setWorkoutId(3);
        detail.setWorkoutTitle("HIIT");
        detail.setWorkoutType("CARDIO");
        detail.setEnrolledAt("2026-11-12");

        Assertions.assertEquals(1, detail.getId());
        Assertions.assertEquals(2, detail.getUserId());
        Assertions.assertEquals("Alice", detail.getUserName());
        Assertions.assertEquals("alice@example.com", detail.getUserEmail());
        Assertions.assertEquals(3, detail.getWorkoutId());
        Assertions.assertEquals("HIIT", detail.getWorkoutTitle());
        Assertions.assertEquals("CARDIO", detail.getWorkoutType());
        Assertions.assertEquals("2026-11-12", detail.getEnrolledAt());
    }
}

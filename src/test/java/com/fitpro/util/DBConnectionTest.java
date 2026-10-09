package com.fitpro.util;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.UserDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.model.CardioWorkout;
import com.fitpro.model.StrengthWorkout;
import com.fitpro.model.Workout;
import com.fitpro.model.YogaWorkout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DBConnectionTest {

    @Test
    public void testJDBCConnectionAndTables() throws Exception {
        // 1. Check Connection
        try (Connection con = DBConnection.getConnection()) {
            Assertions.assertNotNull(con, "JDBC Connection should not be null");
            Assertions.assertFalse(con.isClosed(), "JDBC Connection should be open");

            DatabaseMetaData meta = con.getMetaData();
            System.out.println("[TEST] Connected to database: " + meta.getDatabaseProductName() + " v" + meta.getDatabaseProductVersion());
            System.out.println("[TEST] Driver: " + meta.getDriverName());

            // 2. Check Tables
            Set<String> tables = new HashSet<>();
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES")) {
                while (rs.next()) {
                    tables.add(rs.getString(1).toUpperCase());
                }
            }
            Assertions.assertTrue(tables.contains("USERS"), "Table USERS must exist");
            Assertions.assertTrue(tables.contains("WORKOUTS"), "Table WORKOUTS must exist");
            Assertions.assertTrue(tables.contains("ENROLLMENTS"), "Table ENROLLMENTS must exist");
            Assertions.assertTrue(tables.contains("PROGRESS_LOGS"), "Table PROGRESS_LOGS must exist");
            System.out.println("[TEST] All 4 schema tables verified successfully!");
        }
    }

    @Test
    public void testDAOOperations() throws Exception {
        UserDAO userDAO = new UserDAO();
        WorkoutDAO workoutDAO = new WorkoutDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        Assertions.assertNotNull(userDAO.findAll(), "UserDAO.findAll() should return list");
        Assertions.assertFalse(workoutDAO.findAll().isEmpty(), "WorkoutDAO.findAll() should contain sample workouts");
        Assertions.assertNotNull(enrollmentDAO.findAllEnrollmentDetails(), "EnrollmentDAO.findAllEnrollmentDetails() should not be null");
    }

    @Test
    public void testPolymorphicCalorieCalculations() {
        Workout cardio = Workout.of("CARDIO");
        Workout strength = Workout.of("STRENGTH");
        Workout yoga = Workout.of("YOGA");

        cardio.setMetValue(7.0);
        strength.setMetValue(5.0);
        yoga.setMetValue(3.0);

        Assertions.assertTrue(cardio instanceof CardioWorkout);
        Assertions.assertTrue(strength instanceof StrengthWorkout);
        Assertions.assertTrue(yoga instanceof YogaWorkout);

        double weight = 70.0;
        int minutes = 30;

        double cardioKcal = cardio.caloriesBurned(weight, minutes);
        double strengthKcal = strength.caloriesBurned(weight, minutes);
        double yogaKcal = yoga.caloriesBurned(weight, minutes);

        Assertions.assertTrue(cardioKcal > 0);
        Assertions.assertTrue(strengthKcal > 0);
        Assertions.assertTrue(yogaKcal > 0);
        System.out.println("[TEST] Polymorphic calories calculated: Cardio=" + cardioKcal + ", Strength=" + strengthKcal + ", Yoga=" + yogaKcal);
    }

    @Test
    public void testMultithreadingAndSynchronization() throws Exception {
        com.fitpro.service.NotificationService service = com.fitpro.service.NotificationService.getInstance();
        int initialCount = service.getSentCount();

        // Submit concurrent async notifications
        for (int i = 0; i < 5; i++) {
            service.sendAsync("member" + i + "@example.com", "Workout reminder #" + i);
        }

        // Wait brief period for worker threads to process
        Thread.sleep(600);

        int updatedCount = service.getSentCount();
        Assertions.assertTrue(updatedCount >= initialCount + 1, "Multithreaded counter should increment thread-safely");
        System.out.println("[TEST] Multithreading & Synchronization verified! Sent count: " + updatedCount);
    }

    @Test
    public void testUserPolymorphismAndAbstraction() {
        com.fitpro.model.User member = com.fitpro.model.User.of("MEMBER");
        member.setFullName("Alice Smith");

        com.fitpro.model.User trainer = com.fitpro.model.User.of("TRAINER");
        trainer.setFullName("Coach Bob");

        Assertions.assertEquals("MEMBER", member.getRole());
        Assertions.assertEquals("TRAINER", trainer.getRole());
        Assertions.assertFalse(member.isTrainer());
        Assertions.assertTrue(trainer.isTrainer());
        Assertions.assertTrue(member.getWelcomeMessage().contains("Alice Smith"));
        Assertions.assertTrue(trainer.getWelcomeMessage().contains("Coach Bob"));
        System.out.println("[TEST] User OOP Polymorphism and Abstraction verified!");
    }
}

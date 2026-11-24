package com.fitpro.dao;

import com.fitpro.exception.AppException;
import com.fitpro.model.EnrollmentDetail;
import com.fitpro.model.ProgressLog;
import com.fitpro.model.User;
import com.fitpro.model.Workout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DAOTest {

    private final UserDAO userDAO = new UserDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();

    @Test
    @DisplayName("UserDAO performs CRUD, findByEmail, and findAll correctly")
    public void testUserDAOCRUD() throws AppException {
        String token = UUID.randomUUID().toString().substring(0, 8);
        String name = "DAO Test Member " + token;
        String email = "daouser_" + token + "@fitprotest.com";

        User u = User.of("MEMBER");
        u.setFullName(name);
        u.setEmail(email);
        u.setPasswordHash("dummyHash:123456");

        int id = userDAO.create(u);
        Assertions.assertTrue(id > 0);

        Optional<User> foundById = userDAO.findById(id);
        Assertions.assertTrue(foundById.isPresent());
        Assertions.assertEquals(name, foundById.get().getFullName());

        Optional<User> foundByEmail = userDAO.findByEmail(email);
        Assertions.assertTrue(foundByEmail.isPresent());
        Assertions.assertEquals(id, foundByEmail.get().getId());

        Optional<User> foundByName = userDAO.findByEmailOrName(name);
        Assertions.assertTrue(foundByName.isPresent());
        Assertions.assertEquals(id, foundByName.get().getId());

        Optional<User> foundByEmailOrName = userDAO.findByEmailOrName(email);
        Assertions.assertTrue(foundByEmailOrName.isPresent());
        Assertions.assertEquals(id, foundByEmailOrName.get().getId());

        List<User> all = userDAO.findAll();
        Assertions.assertFalse(all.isEmpty());
    }

    @Test
    @DisplayName("WorkoutDAO inserts and queries by type and id")
    public void testWorkoutDAOCRUD() throws AppException {
        String title = "Sunrise Vinyasa Flow " + UUID.randomUUID().toString().substring(0, 4);
        Workout w = Workout.of("YOGA");
        w.setTitle(title);
        w.setDescription("Calming morning routine");
        w.setLevel("BEGINNER");
        w.setDurationMin(35);
        w.setMetValue(3.2);

        workoutDAO.create(w);

        List<Workout> yogaList = workoutDAO.findByType("YOGA");
        Assertions.assertFalse(yogaList.isEmpty());
        Optional<Workout> created = yogaList.stream().filter(item -> item.getTitle().equals(title)).findFirst();
        Assertions.assertTrue(created.isPresent());
        Assertions.assertEquals("YOGA", created.get().getType());

        Optional<Workout> found = workoutDAO.findById(created.get().getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("YOGA", found.get().getType());
    }

    @Test
    @DisplayName("EnrollmentDAO and ProgressDAO record and retrieve relational entries")
    public void testEnrollmentAndProgressDAO() throws AppException {
        String email = "rel_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User u = User.of("MEMBER");
        u.setFullName("Rel Member");
        u.setEmail(email);
        u.setPasswordHash("hash123:salt");
        int userId = userDAO.create(u);

        List<Workout> workouts = workoutDAO.findAll();
        Assertions.assertFalse(workouts.isEmpty());
        int workoutId = workouts.get(0).getId();

        boolean enrolled = enrollmentDAO.enroll(userId, workoutId);
        Assertions.assertTrue(enrolled);

        List<EnrollmentDetail> allDetails = enrollmentDAO.findAllEnrollmentDetails();
        boolean detailFound = allDetails.stream().anyMatch(d -> d.getUserId() == userId && d.getWorkoutId() == workoutId);
        Assertions.assertTrue(detailFound);

        ProgressLog log = new ProgressLog();
        log.setUserId(userId);
        log.setWorkoutId(workoutId);
        log.setMinutes(25);
        log.setWeightKg(68.0);
        log.setCalories(180.5);
        log.setLoggedOn(LocalDate.now());

        progressDAO.add(log);

        List<ProgressLog> userLogs = progressDAO.findByUser(userId);
        Assertions.assertFalse(userLogs.isEmpty());
        Assertions.assertEquals(25, userLogs.get(0).getMinutes());
    }

    @Test
    @DisplayName("WorkoutDAO search filters by keyword, type, and level")
    public void testWorkoutDAOSearch() throws AppException {
        List<Workout> results = workoutDAO.search("Jog", "CARDIO", "BEGINNER");
        Assertions.assertNotNull(results);
        for (Workout w : results) {
            Assertions.assertEquals("CARDIO", w.getType());
            Assertions.assertEquals("BEGINNER", w.getLevel());
        }
    }

    @Test
    @DisplayName("UserDAO updates role and deletes user cleanly")
    public void testUserRoleUpdateAndDelete() throws AppException {
        String email = "role_test_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User u = User.of("MEMBER");
        u.setFullName("Role Test User");
        u.setEmail(email);
        u.setPasswordHash("hash123:salt");
        int userId = userDAO.create(u);

        boolean updated = userDAO.updateRole(userId, "TRAINER");
        Assertions.assertTrue(updated);

        Optional<User> updatedUser = userDAO.findById(userId);
        Assertions.assertTrue(updatedUser.isPresent());
        Assertions.assertEquals("TRAINER", updatedUser.get().getRole());

        boolean deleted = userDAO.delete(userId);
        Assertions.assertTrue(deleted);
        Assertions.assertFalse(userDAO.findById(userId).isPresent());
    }
}

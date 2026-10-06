package com.fitpro.dao;

import com.fitpro.exception.AppException;
import com.fitpro.model.Workout;
import com.fitpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** JDBC operations on the enrollments table (member <-> workout). */
public class EnrollmentDAO {

    /** @return true if enrolled now, false if the member was already enrolled. */
    public boolean enroll(int userId, int workoutId) throws AppException {
        String sql = "INSERT IGNORE INTO enrollments (user_id, workout_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, workoutId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AppException("Could not enrol in the workout.", e);
        }
    }

    /** IDs of workouts the member joined - a Set gives O(1) "is enrolled?" checks in the JSP. */
    public Set<Integer> findEnrolledIds(int userId) throws AppException {
        Set<Integer> ids = new HashSet<>();
        String sql = "SELECT workout_id FROM enrollments WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
            return ids;
        } catch (SQLException e) {
            throw new AppException("Could not load enrolments.", e);
        }
    }

    /** Full workout objects the member is enrolled in. */
    public List<Workout> findEnrolledWorkouts(int userId) throws AppException {
        WorkoutDAO workoutDAO = new WorkoutDAO();
        List<Workout> result = new ArrayList<>();
        for (int id : findEnrolledIds(userId)) {
            workoutDAO.findById(id).ifPresent(result::add);
        }
        return result;
    }
}

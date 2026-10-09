package com.fitpro.dao;

import com.fitpro.exception.AppException;
import com.fitpro.model.EnrollmentDetail;
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
        if (findEnrolledIds(userId).contains(workoutId)) {
            return false;
        }
        String sql = "INSERT INTO enrollments (user_id, workout_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, workoutId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AppException("Could not enroll in the workout.", e);
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
            throw new AppException("Could not load enrollments.", e);
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

    /** Returns all enrollment details for Admin / Trainer portal reporting. */
    public List<EnrollmentDetail> findAllEnrollmentDetails() throws AppException {
        String sql = "SELECT e.id, e.user_id, u.full_name AS user_name, u.email AS user_email, " +
                     "e.workout_id, w.title AS workout_title, w.type AS workout_type, e.enrolled_at " +
                     "FROM enrollments e " +
                     "JOIN users u ON e.user_id = u.id " +
                     "JOIN workouts w ON e.workout_id = w.id " +
                     "ORDER BY e.id DESC";
        List<EnrollmentDetail> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EnrollmentDetail d = new EnrollmentDetail();
                d.setId(rs.getInt("id"));
                d.setUserId(rs.getInt("user_id"));
                d.setUserName(rs.getString("user_name"));
                d.setUserEmail(rs.getString("user_email"));
                d.setWorkoutId(rs.getInt("workout_id"));
                d.setWorkoutTitle(rs.getString("workout_title"));
                d.setWorkoutType(rs.getString("workout_type"));
                d.setEnrolledAt(rs.getString("enrolled_at"));
                list.add(d);
            }
            return list;
        } catch (SQLException e) {
            throw new AppException("Could not load enrollment records for admin portal.", e);
        }
    }
}

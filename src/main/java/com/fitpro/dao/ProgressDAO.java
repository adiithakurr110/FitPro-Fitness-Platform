package com.fitpro.dao;

import com.fitpro.exception.AppException;
import com.fitpro.model.ProgressLog;
import com.fitpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC operations on the progress_logs table. */
public class ProgressDAO {

    public void add(ProgressLog log) throws AppException {
        String sql = "INSERT INTO progress_logs (user_id, workout_id, minutes, weight_kg, calories, logged_on) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, log.getUserId());
            ps.setInt(2, log.getWorkoutId());
            ps.setInt(3, log.getMinutes());
            ps.setDouble(4, log.getWeightKg());
            ps.setDouble(5, log.getCalories());
            ps.setDate(6, Date.valueOf(log.getLoggedOn()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Could not save your session.", e);
        }
    }

    /** All sessions of a member, newest first. */
    public List<ProgressLog> findByUser(int userId) throws AppException {
        String sql = "SELECT p.id, p.user_id, p.workout_id, w.title, p.minutes, p.weight_kg, p.calories, p.logged_on "
                   + "FROM progress_logs p JOIN workouts w ON p.workout_id = w.id "
                   + "WHERE p.user_id = ? ORDER BY p.logged_on DESC, p.id DESC";
        List<ProgressLog> logs = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProgressLog p = new ProgressLog();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setWorkoutId(rs.getInt("workout_id"));
                    p.setWorkoutTitle(rs.getString("title"));
                    p.setMinutes(rs.getInt("minutes"));
                    p.setWeightKg(rs.getDouble("weight_kg"));
                    p.setCalories(rs.getDouble("calories"));
                    p.setLoggedOn(rs.getDate("logged_on").toLocalDate());
                    logs.add(p);
                }
            }
            return logs;
        } catch (SQLException e) {
            throw new AppException("Could not load your progress.", e);
        }
    }
}

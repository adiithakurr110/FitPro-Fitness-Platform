package com.fitpro.dao;

import com.fitpro.exception.AppException;
import com.fitpro.model.Workout;
import com.fitpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** JDBC operations on the workouts table. */
public class WorkoutDAO implements BaseDAO<Workout, Integer> {

    private static final String SELECT =
            "SELECT w.id, w.title, w.description, w.type, w.level, w.duration_min, w.met_value, "
          + "w.trainer_id, u.full_name AS trainer_name "
          + "FROM workouts w LEFT JOIN users u ON w.trainer_id = u.id ";

    public void create(Workout w) throws AppException {
        String sql = "INSERT INTO workouts (title, description, type, level, duration_min, met_value, trainer_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, w.getTitle());
            ps.setString(2, w.getDescription());
            ps.setString(3, w.getType());
            ps.setString(4, w.getLevel());
            ps.setInt(5, w.getDurationMin());
            ps.setDouble(6, w.getMetValue());
            ps.setInt(7, w.getTrainerId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Could not save the workout.", e);
        }
    }

    @Override
    public Optional<Workout> findById(Integer id) throws AppException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT + "WHERE w.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AppException("Could not load the workout.", e);
        }
    }

    @Override
    public List<Workout> findAll() throws AppException {
        return query(SELECT + "ORDER BY w.id", null);
    }

    /** Workouts filtered by category (CARDIO / STRENGTH / YOGA). */
    public List<Workout> findByType(String type) throws AppException {
        return query(SELECT + "WHERE w.type = ? ORDER BY w.id", type);
    }

    private List<Workout> query(String sql, String param) throws AppException {
        List<Workout> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (param != null) ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new AppException("Could not load workouts.", e);
        }
    }

    private Workout map(ResultSet rs) throws SQLException {
        Workout w = Workout.of(rs.getString("type"));
        w.setId(rs.getInt("id"));
        w.setTitle(rs.getString("title"));
        w.setDescription(rs.getString("description"));
        w.setLevel(rs.getString("level"));
        w.setDurationMin(rs.getInt("duration_min"));
        w.setMetValue(rs.getDouble("met_value"));
        w.setTrainerId(rs.getInt("trainer_id"));
        w.setTrainerName(rs.getString("trainer_name"));
        return w;
    }
}

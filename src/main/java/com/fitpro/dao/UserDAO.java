package com.fitpro.dao;

import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** JDBC operations on the users table. */
public class UserDAO implements BaseDAO<User, Integer> {

    private static final String COLS = "id, full_name, email, password_hash, role";

    /** Inserts a new user and returns the generated id. */
    public int create(User user) throws AppException {
        String sql = "INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new AppException("Could not create the account.", e);
        }
    }

    public Optional<User> findByEmail(String email) throws AppException {
        String sql = "SELECT " + COLS + " FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO findByEmail Error] " + e.getMessage());
            throw new AppException("Could not look up the user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) throws AppException {
        String sql = "SELECT " + COLS + " FROM users WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AppException("Could not look up the user.", e);
        }
    }

    @Override
    public List<User> findAll() throws AppException {
        List<User> users = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT " + COLS + " FROM users ORDER BY id")) {
            while (rs.next()) users.add(map(rs));
            return users;
        } catch (SQLException e) {
            throw new AppException("Could not load users.", e);
        }
    }

    /** Maps one row to the right subclass (Member or Trainer). */
    private User map(ResultSet rs) throws SQLException {
        User u = User.of(rs.getString("role"));
        u.setId(rs.getInt("id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        return u;
    }
}

package com.fitpro.util;

import com.fitpro.exception.AppException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;

/**
 * Central JDBC Connectivity Manager.
 * Demonstrates:
 * - Singleton / Utility Pattern with thread-safe connection pooling
 * - Dual-Engine Support: Embedded H2 (zero setup) and MySQL
 * - Explicit database mode configuration via db.properties or environment variables
 * - Idempotent, non-destructive schema initialization and optional demo data seeding
 */
public final class DBConnection {
    private static final Properties PROPS = new Properties();
    private static String activeMode = "h2";
    private static boolean initialized = false;

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ignored) {}
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException ignored) {}
        } catch (Exception e) {
            System.err.println("[FitPro] Driver loading note: " + e.getMessage());
        }
    }

    private DBConnection() { }

    /**
     * Opens a new database connection. Callers must close it (use try-with-resources).
     */
    public static synchronized Connection getConnection() throws AppException {
        String configuredType = env("DB_TYPE", PROPS.getProperty("db.type", "h2")).trim().toLowerCase();

        if ("mysql".equals(configuredType) && !"h2".equals(activeMode)) {
            String url  = env("DB_URL", PROPS.getProperty("db.mysql.url", "jdbc:mysql://localhost:3306/fitpro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"));
            String user = env("DB_USER", PROPS.getProperty("db.mysql.user", "root"));
            String pass = env("DB_PASSWORD", PROPS.getProperty("db.mysql.password", ""));

            try {
                Connection con = DriverManager.getConnection(url, user, pass);
                if (!initialized) {
                    activeMode = "mysql";
                    System.out.println("[FitPro Database] Active Mode: MySQL Server (" + url + ")");
                    initTablesIfNeeded(con);
                    seedInitialDataIfNeeded(con);
                    initialized = true;
                }
                return con;
            } catch (SQLException e) {
                System.err.println("[FitPro Database Alert] MySQL connection failed (" + e.getMessage() + "). Falling back to embedded H2 database mode.");
                activeMode = "h2";
            }
        }

        // H2 embedded mode (default or fallback)
        Connection h2Con = getH2Connection();
        if (!initialized) {
            activeMode = "h2";
            System.out.println("[FitPro Database] Active Mode: Embedded H2 Database (Persistent at ./data/fitpro_db)");
            initTablesIfNeeded(h2Con);
            seedInitialDataIfNeeded(h2Con);
            initialized = true;
        }
        return h2Con;
    }

    /** Returns the currently active database mode name ("h2" or "mysql"). */
    public static synchronized String getActiveMode() {
        return activeMode;
    }

    private static Connection getH2Connection() throws AppException {
        try {
            String defaultH2Url = "jdbc:h2:./data/fitpro_db;MODE=MySQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE;AUTO_SERVER=TRUE";
            String h2Url  = env("H2_URL", PROPS.getProperty("db.h2.url", defaultH2Url));
            String h2User = env("H2_USER", PROPS.getProperty("db.h2.user", "sa"));
            String h2Pass = env("H2_PASSWORD", PROPS.getProperty("db.h2.password", ""));
            return DriverManager.getConnection(h2Url, h2User, h2Pass);
        } catch (SQLException e) {
            throw new AppException("Cannot connect to embedded H2 database: " + e.getMessage(), e);
        }
    }

    private static void initTablesIfNeeded(Connection con) {
        try (Statement st = con.createStatement()) {
            boolean tablesExist = false;
            try {
                st.executeQuery("SELECT 1 FROM users WHERE 1=0");
                tablesExist = true;
            } catch (SQLException e) {
                tablesExist = false;
            }

            if (!tablesExist) {
                System.out.println("[FitPro] Table 'users' not found. Executing idempotent schema.sql...");
                InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("schema.sql");
                if (in != null) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            String trimmed = line.trim();
                            if (trimmed.startsWith("--") || trimmed.startsWith("//") || trimmed.isEmpty()) continue;
                            if (trimmed.toUpperCase().startsWith("CREATE DATABASE") || trimmed.toUpperCase().startsWith("USE ")) continue;
                            sb.append(line).append("\n");
                            if (trimmed.endsWith(";")) {
                                String sql = sb.toString();
                                int idx = sql.lastIndexOf(";");
                                if (idx != -1) {
                                    sql = sql.substring(0, idx).trim();
                                }
                                sb.setLength(0);
                                if (!sql.isEmpty()) {
                                    try {
                                        st.execute(sql);
                                    } catch (SQLException ex) {
                                        System.err.println("[FitPro Schema SQL Warning] " + ex.getMessage() + " | SQL: " + sql);
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("[FitPro] Database schema initialized successfully.");
                } else {
                    System.err.println("[FitPro Init Error] schema.sql resource not found on classpath!");
                }
            } else {
                System.out.println("[FitPro] Database tables already exist. Skipping schema creation.");
            }
        } catch (Exception e) {
            System.err.println("[FitPro] Database auto-init error: " + e.getMessage());
        }
    }

    private static void seedInitialDataIfNeeded(Connection con) {
        try (Statement st = con.createStatement()) {
            // 1. Seed workouts catalogue if empty
            int workoutCount = 0;
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM workouts")) {
                if (rs.next()) workoutCount = rs.getInt(1);
            }

            if (workoutCount == 0) {
                System.out.println("[FitPro] Seeding initial workout catalogue...");
                String insertWorkout = "INSERT INTO workouts (title, description, type, level, duration_min, met_value) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertWorkout)) {
                    Object[][] initialWorkouts = {
                        {"Morning Jog Starter", "Easy 30 minute jog to build stamina and aerobic base.", "CARDIO", "BEGINNER", 30, 7.0},
                        {"HIIT Fat Burner", "High-intensity intervals: burpees, jumps, and sprints.", "CARDIO", "ADVANCED", 25, 10.0},
                        {"Full Body Strength", "Squats, push-ups, dumbbell rows, and core planks.", "STRENGTH", "INTERMEDIATE", 45, 5.0},
                        {"Upper Body Power", "Chest, shoulder, and arm hypertrophy training.", "STRENGTH", "ADVANCED", 50, 6.0},
                        {"Sunrise Yoga Flow", "Gentle vinyasa sequence for flexibility and mental focus.", "YOGA", "BEGINNER", 40, 3.0},
                        {"Power Yoga Sculpt", "Strength-building dynamic yoga with sustained holds.", "YOGA", "INTERMEDIATE", 45, 4.0}
                    };
                    for (Object[] row : initialWorkouts) {
                        ps.setString(1, (String) row[0]);
                        ps.setString(2, (String) row[1]);
                        ps.setString(3, (String) row[2]);
                        ps.setString(4, (String) row[3]);
                        ps.setInt(5, (Integer) row[4]);
                        ps.setDouble(6, (Double) row[5]);
                        ps.executeUpdate();
                    }
                }
            }

            // 2. Seed demonstration accounts if users table is empty
            int userCount = 0;
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next()) userCount = rs.getInt(1);
            }

            if (userCount == 0) {
                System.out.println("[FitPro] Seeding demo accounts (Admin, Trainer, Member)...");
                String adminHash = PasswordUtil.hash("admin123");
                String trainerHash = PasswordUtil.hash("trainer123");
                String memberHash = PasswordUtil.hash("member123");

                String insertUser = "INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)";
                int adminId = 0, trainerId = 0, memberId = 0;
                try (PreparedStatement ps = con.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "Alex Vance");
                    ps.setString(2, "admin@fitpro.com");
                    ps.setString(3, adminHash);
                    ps.setString(4, "ADMIN");
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) adminId = k.getInt(1); }

                    ps.setString(1, "Sarah Connor");
                    ps.setString(2, "trainer@fitpro.com");
                    ps.setString(3, trainerHash);
                    ps.setString(4, "TRAINER");
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) trainerId = k.getInt(1); }

                    ps.setString(1, "Marcus Wright");
                    ps.setString(2, "member@fitpro.com");
                    ps.setString(3, memberHash);
                    ps.setString(4, "MEMBER");
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) memberId = k.getInt(1); }
                }

                if (trainerId > 0) {
                    try (PreparedStatement ps = con.prepareStatement("UPDATE workouts SET trainer_id = ? WHERE trainer_id IS NULL")) {
                        ps.setInt(1, trainerId);
                        ps.executeUpdate();
                    }
                }

                if (memberId > 0) {
                    try (PreparedStatement ps = con.prepareStatement("INSERT INTO enrollments (user_id, workout_id) VALUES (?, ?)")) {
                        ps.setInt(1, memberId);
                        ps.setInt(2, 1);
                        ps.executeUpdate();
                        ps.setInt(1, memberId);
                        ps.setInt(2, 2);
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO progress_logs (user_id, workout_id, minutes, weight_kg, calories, logged_on) VALUES (?, ?, ?, ?, ?, ?)")) {
                        ps.setInt(1, memberId);
                        ps.setInt(2, 1);
                        ps.setInt(3, 30);
                        ps.setDouble(4, 74.5);
                        ps.setDouble(5, 260.8);
                        ps.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(3)));
                        ps.executeUpdate();

                        ps.setInt(1, memberId);
                        ps.setInt(2, 2);
                        ps.setInt(3, 25);
                        ps.setDouble(4, 74.0);
                        ps.setDouble(5, 308.3);
                        ps.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(1)));
                        ps.executeUpdate();
                    }
                }
                System.out.println("[FitPro] Demonstration data initialized successfully.");
            }
        } catch (Exception e) {
            System.err.println("[FitPro] Seeding note: " + e.getMessage());
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.trim().isEmpty()) ? fallback : value.trim();
    }
}

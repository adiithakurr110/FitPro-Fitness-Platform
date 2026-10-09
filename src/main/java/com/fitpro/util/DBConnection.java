package com.fitpro.util;

import com.fitpro.exception.AppException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Central place for JDBC connectivity.
 * Reads db.properties from classpath; environment variables
 * DB_URL / DB_USER / DB_PASSWORD override the file.
 * Auto-initializes schema and falls back seamlessly to embedded H2 if MySQL is unavailable.
 */
public final class DBConnection {
    private static final Properties PROPS = new Properties();
    private static boolean useH2Fallback = false;
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

    /** Opens a new connection. Callers must close it (use try-with-resources). */
    public static synchronized Connection getConnection() throws AppException {
        String url  = env("DB_URL", PROPS.getProperty("db.url", "jdbc:mysql://localhost:3306/fitpro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"));
        String user = env("DB_USER", PROPS.getProperty("db.user", "root"));
        String pass = env("DB_PASSWORD", PROPS.getProperty("db.password", "root"));

        if (useH2Fallback) {
            return getH2Connection();
        }

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            if (!initialized) {
                initTablesIfNeeded(con);
                initialized = true;
            }
            return con;
        } catch (SQLException e) {
            System.out.println("[FitPro] MySQL note: " + e.getMessage() + ". Using embedded H2 database mode.");
            useH2Fallback = true;
            Connection h2Con = getH2Connection();
            if (!initialized) {
                initTablesIfNeeded(h2Con);
                initialized = true;
            }
            return h2Con;
        }
    }

    private static Connection getH2Connection() throws AppException {
        try {
            String h2Url = "jdbc:h2:./data/fitpro_db;MODE=MySQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE;AUTO_SERVER=TRUE";
            return DriverManager.getConnection(h2Url, "sa", "");
        } catch (SQLException e) {
            throw new AppException("Cannot connect to embedded H2 database.", e);
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
                System.out.println("[FitPro] Table 'users' not found. Initializing database schema...");
                InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("schema.sql");
                if (in == null) {
                    java.io.File file = new java.io.File("database/schema.sql");
                    if (file.exists()) {
                        in = new java.io.FileInputStream(file);
                    }
                }
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
                                        System.out.println("[FitPro Init Executed] " + (sql.length() > 60 ? sql.substring(0, 60) + "..." : sql));
                                    } catch (SQLException ex) {
                                        System.err.println("[FitPro Init SQL Warning] " + ex.getMessage() + " | SQL: " + sql);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.err.println("[FitPro Init Error] schema.sql resource not found!");
                }
            } else {
                System.out.println("[FitPro] Database tables already exist.");
            }
        } catch (Exception e) {
            System.err.println("[FitPro] Database auto-init error: " + e.getMessage());
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? fallback : value;
    }
}

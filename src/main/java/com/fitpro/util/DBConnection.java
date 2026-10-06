package com.fitpro.util;

import com.fitpro.exception.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Central place for JDBC connectivity.
 * Reads db.properties from the classpath; environment variables
 * DB_URL / DB_USER / DB_PASSWORD override the file (handy for deployment).
 */
public final class DBConnection {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            PROPS.load(in);
            Class.forName(PROPS.getProperty("db.driver"));   // load the JDBC driver
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() { }

    /** Opens a new connection. Callers must close it (use try-with-resources). */
    public static Connection getConnection() throws AppException {
        String url  = env("DB_URL", PROPS.getProperty("db.url"));
        String user = env("DB_USER", PROPS.getProperty("db.user"));
        String pass = env("DB_PASSWORD", PROPS.getProperty("db.password"));
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new AppException("Cannot connect to the database. Check db.properties and that MySQL is running.", e);
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? fallback : value;
    }
}

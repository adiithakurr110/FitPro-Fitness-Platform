package com.fitpro.listener;

import com.fitpro.service.NotificationService;
import com.fitpro.util.DBConnection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;

/** Initializes database and stops background threads cleanly when web app shuts down. */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[FitPro] Application starting up... Checking database initialization...");
        try (Connection con = DBConnection.getConnection()) {
            System.out.println("[FitPro] Database connection verified.");
        } catch (Exception e) {
            System.err.println("[FitPro] Startup database init note: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        NotificationService.getInstance().shutdown();
    }
}

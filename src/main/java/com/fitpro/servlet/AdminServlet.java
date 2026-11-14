package com.fitpro.servlet;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.ProgressDAO;
import com.fitpro.dao.UserDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Dedicated Administrator Management Portal.
 * Restricts access strictly to accounts with the ADMIN role.
 * Provides user & role management, workout content moderation,
 * platform telemetry, and system diagnostics.
 */
@WebServlet("/app/admin")
public class AdminServlet extends HttpServlet {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            req.getSession().setAttribute("error", "Access denied. Administrator privileges are required to access the Admin Portal.");
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        loadAdminData(req);
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("updateRole".equals(action)) {
                int targetUserId = Integer.parseInt(req.getParameter("userId"));
                String newRole = req.getParameter("newRole");
                if ("MEMBER".equalsIgnoreCase(newRole) || "TRAINER".equalsIgnoreCase(newRole) || "ADMIN".equalsIgnoreCase(newRole)) {
                    userDAO.updateRole(targetUserId, newRole);
                    req.setAttribute("success", "User role successfully updated to " + newRole.toUpperCase() + ".");
                } else {
                    req.setAttribute("error", "Invalid role selected.");
                }
            } else if ("deleteUser".equals(action)) {
                int targetUserId = Integer.parseInt(req.getParameter("userId"));
                if (targetUserId == user.getId()) {
                    req.setAttribute("error", "You cannot delete your own administrator account.");
                } else {
                    userDAO.delete(targetUserId);
                    req.setAttribute("success", "User account and associated records deleted.");
                }
            } else if ("deleteWorkout".equals(action)) {
                int workoutId = Integer.parseInt(req.getParameter("workoutId"));
                workoutDAO.delete(workoutId);
                req.setAttribute("success", "Workout program removed from catalog.");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid identifier provided.");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }

        loadAdminData(req);
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    private void loadAdminData(HttpServletRequest req) {
        try {
            req.setAttribute("users", userDAO.findAll());
            req.setAttribute("workouts", workoutDAO.findAll());
            req.setAttribute("enrollments", enrollmentDAO.findAllEnrollmentDetails());
            req.setAttribute("logs", progressDAO.findAll());

            // Telemetry & diagnostics for Admin Console
            Runtime rt = Runtime.getRuntime();
            long totalMb = rt.totalMemory() / (1024 * 1024);
            long freeMb = rt.freeMemory() / (1024 * 1024);
            long usedMb = totalMb - freeMb;
            req.setAttribute("jvmUsedMemory", usedMb);
            req.setAttribute("jvmTotalMemory", totalMb);
            req.setAttribute("jvmAvailableProcessors", rt.availableProcessors());
            req.setAttribute("javaVersion", System.getProperty("java.version"));
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
    }
}

package com.fitpro.servlet;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.ProgressDAO;
import com.fitpro.dao.UserDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Dedicated Administrator Management Portal.
 * Restricts access strictly to accounts with the ADMIN role.
 * Implements Post/Redirect/Get (PRG) pattern for state changes to prevent form re-submissions.
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

        // Consume session flash messages if present
        HttpSession session = req.getSession();
        String flashSuccess = (String) session.getAttribute("success");
        if (flashSuccess != null) {
            req.setAttribute("success", flashSuccess);
            session.removeAttribute("success");
        }
        String flashError = (String) session.getAttribute("error");
        if (flashError != null) {
            req.setAttribute("error", flashError);
            session.removeAttribute("error");
        }

        loadAdminData(req);
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        try {
            if ("updateRole".equals(action)) {
                int targetUserId = Integer.parseInt(req.getParameter("userId"));
                String newRole = req.getParameter("newRole");

                // Prevent admin from demoting their own superuser account
                if (targetUserId == user.getId() && !"ADMIN".equalsIgnoreCase(newRole)) {
                    session.setAttribute("error", "You cannot demote your own administrator account.");
                } else if ("MEMBER".equalsIgnoreCase(newRole) || "TRAINER".equalsIgnoreCase(newRole) || "ADMIN".equalsIgnoreCase(newRole)) {
                    userDAO.updateRole(targetUserId, newRole);
                    session.setAttribute("success", "User role successfully updated to " + newRole.toUpperCase() + ".");
                } else {
                    session.setAttribute("error", "Invalid role selected.");
                }
            } else if ("deleteUser".equals(action)) {
                int targetUserId = Integer.parseInt(req.getParameter("userId"));
                // Prevent admin from deleting their own superuser account
                if (targetUserId == user.getId()) {
                    session.setAttribute("error", "You cannot delete your own administrator account.");
                } else {
                    userDAO.delete(targetUserId);
                    session.setAttribute("success", "User account and associated records deleted.");
                }
            } else if ("deleteWorkout".equals(action)) {
                int workoutId = Integer.parseInt(req.getParameter("workoutId"));
                workoutDAO.delete(workoutId);
                session.setAttribute("success", "Workout program removed from the catalogue.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid identifier provided.");
        } catch (AppException e) {
            session.setAttribute("error", e.getMessage());
        }

        // Post/Redirect/Get pattern prevents duplicate submissions on refresh
        resp.sendRedirect(req.getContextPath() + "/app/admin");
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
            req.setAttribute("dbMode", DBConnection.getActiveMode().toUpperCase());
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
    }
}

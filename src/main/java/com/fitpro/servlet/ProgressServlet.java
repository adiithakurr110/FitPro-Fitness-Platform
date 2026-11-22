package com.fitpro.servlet;

import com.fitpro.exception.AppException;
import com.fitpro.model.ProgressLog;
import com.fitpro.model.User;
import com.fitpro.service.ProgressService;
import com.fitpro.service.WorkoutService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Members log training sessions, inspect historical data, and export CSV progress reports.
 * Employs Post/Redirect/Get (PRG) pattern to prevent duplicate session records on refresh.
 */
@WebServlet("/app/progress")
public class ProgressServlet extends HttpServlet {
    private final ProgressService progressService = new ProgressService();
    private final WorkoutService workoutService = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        // Innovation feature: Export session logs to CSV spreadsheet file
        if ("export".equalsIgnoreCase(action) && !user.isTrainer() && !user.isAdmin()) {
            exportCsv(resp, user.getId());
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

        render(req, resp, user);
    }

    private void exportCsv(HttpServletResponse resp, int userId) throws IOException {
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"fitpro_progress_report.csv\"");
        try {
            List<ProgressLog> logs = progressService.history(userId);
            try (PrintWriter writer = resp.getWriter()) {
                writer.println("Date,Workout Title,Duration (min),Body Weight (kg),Calories Burned (kcal)");
                for (ProgressLog log : logs) {
                    String title = log.getWorkoutTitle() != null ? log.getWorkoutTitle().replace("\"", "\"\"") : "Workout";
                    writer.printf("%s,\"%s\",%d,%.1f,%.1f%n",
                            log.getLoggedOn(),
                            title,
                            log.getMinutes(),
                            log.getWeightKg(),
                            log.getCalories());
                }
            }
        } catch (AppException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not export logs: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (user.isTrainer() || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        HttpSession session = req.getSession();
        try {
            ProgressLog log = progressService.logSession(user.getId(),
                    Integer.parseInt(req.getParameter("workoutId")),
                    req.getParameter("minutes"), req.getParameter("weight"));
            session.setAttribute("success", "Session saved! " + log.getCalories() + " kcal burned successfully.");
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Please select a workout program and enter valid numbers.");
        } catch (AppException e) {
            session.setAttribute("error", e.getMessage());
        }

        // Post/Redirect/Get pattern prevents duplicate session entries on refresh
        resp.sendRedirect(req.getContextPath() + "/app/progress");
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            if (!user.isTrainer() && !user.isAdmin()) {
                req.setAttribute("myWorkouts", workoutService.enrolledWorkouts(user.getId()));
                req.setAttribute("logs", progressService.history(user.getId()));
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/progress.jsp").forward(req, resp);
    }
}

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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/** Members log finished sessions, see their history, and export progress reports. */
@WebServlet("/app/progress")
public class ProgressServlet extends HttpServlet {
    private final ProgressService progressService = new ProgressService();
    private final WorkoutService workoutService = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");

        // Innovation feature: Export session logs to CSV report
        if ("export".equalsIgnoreCase(action) && user != null && !user.isTrainer() && !user.isAdmin()) {
            exportCsv(resp, user.getId());
            return;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user.isTrainer() || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }
        try {
            ProgressLog log = progressService.logSession(user.getId(),
                    Integer.parseInt(req.getParameter("workoutId")),
                    req.getParameter("minutes"), req.getParameter("weight"));
            req.setAttribute("success", "Session saved: about " + log.getCalories() + " kcal burned.");
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Please choose a workout.");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        render(req, resp, user);
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            if (user != null && !user.isTrainer() && !user.isAdmin()) {
                req.setAttribute("myWorkouts", workoutService.enrolledWorkouts(user.getId()));
                req.setAttribute("logs", progressService.history(user.getId()));
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/progress.jsp").forward(req, resp);
    }
}

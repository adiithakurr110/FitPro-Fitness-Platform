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

/** Members log finished sessions and see their history. */
@WebServlet("/app/progress")
public class ProgressServlet extends HttpServlet {
    private final ProgressService progressService = new ProgressService();
    private final WorkoutService workoutService = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        render(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user.isTrainer()) {
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
            if (!user.isTrainer()) {
                req.setAttribute("myWorkouts", workoutService.enrolledWorkouts(user.getId()));
                req.setAttribute("logs", progressService.history(user.getId()));
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/progress.jsp").forward(req, resp);
    }
}

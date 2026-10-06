package com.fitpro.servlet;

import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.service.WorkoutService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Workout catalogue.
 * GET  -> list (optional ?type=CARDIO filter)
 * POST -> action=enroll (members) or action=create (trainers)
 */
@WebServlet("/app/workouts")
public class WorkoutServlet extends HttpServlet {
    private final WorkoutService service = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        showList(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            if ("enroll".equals(action)) {
                service.enroll(user, Integer.parseInt(req.getParameter("workoutId")));
                req.setAttribute("success", "You are enrolled! Find it on your dashboard.");
            } else if ("create".equals(action)) {
                service.publish(user, req.getParameter("title"), req.getParameter("description"),
                        req.getParameter("type"), req.getParameter("level"),
                        req.getParameter("duration"), req.getParameter("met"));
                req.setAttribute("success", "Program published.");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid workout selected.");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        showList(req, resp, user);
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            String type = req.getParameter("type");
            req.setAttribute("workouts", service.list(type));
            req.setAttribute("activeType", type == null ? "" : type.toUpperCase());
            if (!user.isTrainer()) req.setAttribute("enrolledIds", service.enrolledIds(user.getId()));
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/workouts.jsp").forward(req, resp);
    }
}

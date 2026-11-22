package com.fitpro.servlet;

import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.service.WorkoutService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Workout catalogue controller.
 * GET  -> list and search (supports ?type=CARDIO and ?q=keyword)
 * POST -> state-changing actions (action=enroll, create, delete) using PRG pattern
 */
@WebServlet("/app/workouts")
public class WorkoutServlet extends HttpServlet {
    private final WorkoutService service = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Consume session flash messages if present (PRG pattern)
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

        showList(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        try {
            if ("enroll".equals(action)) {
                if (user.isTrainer() || user.isAdmin()) {
                    session.setAttribute("error", "Trainers and administrators cannot enroll in workouts.");
                } else {
                    service.enroll(user, Integer.parseInt(req.getParameter("workoutId")));
                    session.setAttribute("success", "You are enrolled! The workout routine is now active on your dashboard.");
                }
            } else if ("create".equals(action)) {
                service.publish(user, req.getParameter("title"), req.getParameter("description"),
                        req.getParameter("type"), req.getParameter("level"),
                        req.getParameter("duration"), req.getParameter("met"));
                session.setAttribute("success", "New workout program published to catalogue.");
            } else if ("delete".equals(action)) {
                service.delete(user, Integer.parseInt(req.getParameter("workoutId")));
                session.setAttribute("success", "Workout program removed successfully.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid workout identifier provided.");
        } catch (AppException e) {
            session.setAttribute("error", e.getMessage());
        }

        // Redirect after POST prevents accidental duplicate enrollment or creation on page refresh
        resp.sendRedirect(req.getContextPath() + "/app/workouts");
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            String type = req.getParameter("type");
            String query = req.getParameter("q");
            String level = req.getParameter("level");

            if ((query != null && !query.trim().isEmpty()) || (level != null && !level.trim().isEmpty())) {
                req.setAttribute("workouts", service.search(query, type, level));
            } else {
                req.setAttribute("workouts", service.list(type));
            }
            req.setAttribute("activeType", type == null ? "" : type.toUpperCase());
            req.setAttribute("searchQuery", query == null ? "" : query.trim());
            if (!user.isTrainer() && !user.isAdmin()) {
                req.setAttribute("enrolledIds", service.enrolledIds(user.getId()));
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/workouts.jsp").forward(req, resp);
    }
}

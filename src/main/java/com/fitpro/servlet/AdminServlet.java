package com.fitpro.servlet;

import com.fitpro.dao.EnrollmentDAO;
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

/** Admin & Trainer management portal showing all enrolled users and system metrics. */
@WebServlet("/app/admin")
public class AdminServlet extends HttpServlet {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.isTrainer()) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        try {
            req.setAttribute("enrollments", enrollmentDAO.findAllEnrollmentDetails());
            req.setAttribute("users", userDAO.findAll());
            req.setAttribute("workouts", workoutDAO.findAll());
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }
}

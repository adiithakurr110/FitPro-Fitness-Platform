package com.fitpro.servlet;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.UserDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.service.ProgressService;
import com.fitpro.service.WorkoutService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Home page after login: stats and enrolled programs for members, system summary for trainers. */
@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {
    private final ProgressService progressService = new ProgressService();
    private final WorkoutService workoutService = new WorkoutService();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            if (!user.isTrainer()) {
                req.setAttribute("summary", progressService.summarize(user.getId()));
                req.setAttribute("myWorkouts", workoutService.enrolledWorkouts(user.getId()));
            } else {
                req.setAttribute("allWorkouts", workoutDAO.findAll());
                req.setAttribute("allUsers", userDAO.findAll());
                req.setAttribute("allEnrollments", enrollmentDAO.findAllEnrollmentDetails());
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}

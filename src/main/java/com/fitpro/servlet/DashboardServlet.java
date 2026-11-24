package com.fitpro.servlet;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.ProgressDAO;
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

/**
 * Adaptive Home Page after login:
 * - Members: personal metrics, enrolled workout plans, calorie charts, BMI target tool, diet guidance.
 * - Trainers: coach studio, assigned members, member progress feeds, workout plan management.
 * - Administrators: executive overview, platform health, quick user & workout management.
 */
@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {
    private final ProgressService progressService = new ProgressService();
    private final WorkoutService workoutService = new WorkoutService();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?authRequired=1");
            return;
        }

        // Consume flash messages if set in session
        String flashErr = (String) req.getSession().getAttribute("error");
        if (flashErr != null) {
            req.setAttribute("error", flashErr);
            req.getSession().removeAttribute("error");
        }
        String flashSuccess = (String) req.getSession().getAttribute("success");
        if (flashSuccess != null) {
            req.setAttribute("success", flashSuccess);
            req.getSession().removeAttribute("success");
        }

        try {
            if (user.isAdmin()) {
                req.setAttribute("allWorkouts", workoutDAO.findAll());
                req.setAttribute("allUsers", userDAO.findAll());
                req.setAttribute("allEnrollments", enrollmentDAO.findAllEnrollmentDetails());
                req.setAttribute("allLogs", progressDAO.findAll());
            } else if (user.isTrainer()) {
                req.setAttribute("allWorkouts", workoutDAO.findAll());
                req.setAttribute("allUsers", userDAO.findAll());
                req.setAttribute("allEnrollments", enrollmentDAO.findAllEnrollmentDetails());
                req.setAttribute("recentMemberLogs", progressDAO.findAll());
            } else {
                req.setAttribute("summary", progressService.summarize(user.getId()));
                req.setAttribute("myWorkouts", workoutService.enrolledWorkouts(user.getId()));
                req.setAttribute("recentLogs", progressService.history(user.getId()));
            }
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

package com.fitpro.servlet;

import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication Controller:
 * GET displays the login view with relevant status alerts.
 * POST validates credentials, prevents session fixation, and starts an authenticated session.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        if (req.getParameter("registered") != null) {
            req.setAttribute("success", "Account created successfully! Please sign in with your credentials.");
        }
        if (req.getParameter("loggedOut") != null) {
            req.setAttribute("success", "You have been successfully signed out of FitPro.");
        }
        if (req.getParameter("authRequired") != null) {
            req.setAttribute("error", "Please sign in to access your dashboard and workout features.");
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String identifier = req.getParameter("email");
        if (identifier == null || identifier.trim().isEmpty()) {
            identifier = req.getParameter("username");
        }
        String password = req.getParameter("password");

        try {
            User user = authService.login(identifier, password);
            HttpSession old = req.getSession(false);
            if (old != null) {
                old.invalidate(); // Prevent session fixation
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", identifier);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}

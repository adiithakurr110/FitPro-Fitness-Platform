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

/** GET shows the login form, POST checks credentials and starts a session. */
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
            req.setAttribute("success", "Account created! Please log in.");
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.login(req.getParameter("email"), req.getParameter("password"));
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();             // prevent session fixation
            req.getSession(true).setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", req.getParameter("email"));
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}

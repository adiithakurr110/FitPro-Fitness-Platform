package com.fitpro.servlet;

import com.fitpro.exception.AppException;
import com.fitpro.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** GET shows the sign-up form, POST creates the account. */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            authService.register(req.getParameter("fullName"), req.getParameter("email"),
                    req.getParameter("password"), req.getParameter("role"));
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("fullName", req.getParameter("fullName"));
            req.setAttribute("email", req.getParameter("email"));
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}

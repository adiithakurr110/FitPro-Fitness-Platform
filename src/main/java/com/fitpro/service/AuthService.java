package com.fitpro.service;

import com.fitpro.dao.UserDAO;
import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import com.fitpro.util.PasswordUtil;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Business rules for authentication, registration, and credential security.
 * Enforces server-side validation, password hashing, and role safety.
 */
public class AuthService {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private final UserDAO userDAO = new UserDAO();

    /**
     * Validates input, hashes the password, and stores a new user.
     * Enforces RBAC safety: Registration is strictly limited to MEMBER and TRAINER.
     * Public visitors cannot self-register as ADMIN.
     */
    public User register(String name, String email, String password, String role) throws AppException {
        if (name == null || name.trim().length() < 2)
            throw new AppException("Please enter your full name (minimum 2 characters).");
        if (email == null || !EMAIL.matcher(email.trim()).matches())
            throw new AppException("Please enter a valid email address.");
        if (password == null || password.length() < 6)
            throw new AppException("Password must be at least 6 characters.");
        if (userDAO.findByEmail(email.trim().toLowerCase()).isPresent())
            throw new AppException("An account with this email already exists.");

        // Security check: Never grant ADMIN privilege from a registration form
        String safeRole = "TRAINER".equalsIgnoreCase(role) ? "TRAINER" : "MEMBER";

        User user = User.of(safeRole);
        user.setFullName(name.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setId(userDAO.create(user));

        NotificationService.getInstance().sendAsync(user.getEmail(), "Welcome to FitPro, " + user.getFullName() + "!");
        return user;
    }

    /**
     * Authenticates an account against stored salted PBKDF2 hashes.
     * @throws AppException with a deliberately generic message on failure to prevent user enumeration
     */
    public User login(String email, String password) throws AppException {
        Optional<User> found = userDAO.findByEmail(email == null ? "" : email.trim().toLowerCase());
        if (!found.isPresent() || password == null
                || !PasswordUtil.verify(password, found.get().getPasswordHash())) {
            throw new AppException("Invalid email or password.");
        }
        return found.get();
    }
}

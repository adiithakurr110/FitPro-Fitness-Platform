package com.fitpro.service;

import com.fitpro.exception.AppException;
import com.fitpro.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    @DisplayName("Registration rejects invalid name")
    public void testRegisterInvalidName() {
        Assertions.assertThrows(AppException.class, () ->
                authService.register("", "user@example.com", "password123", "MEMBER"));

        Assertions.assertThrows(AppException.class, () ->
                authService.register("   ", "user@example.com", "password123", "MEMBER"));
    }

    @Test
    @DisplayName("Registration rejects invalid email format")
    public void testRegisterInvalidEmail() {
        Assertions.assertThrows(AppException.class, () ->
                authService.register("Valid Name", "notanemail", "password123", "MEMBER"));

        Assertions.assertThrows(AppException.class, () ->
                authService.register("Valid Name", "missing@domain", "password123", "MEMBER"));
    }

    @Test
    @DisplayName("Registration rejects short password")
    public void testRegisterShortPassword() {
        Assertions.assertThrows(AppException.class, () ->
                authService.register("Valid Name", "user@example.com", "123", "MEMBER"));
    }

    @Test
    @DisplayName("Successful registration and login workflow")
    public void testRegisterAndLoginSuccess() throws Exception {
        String token = UUID.randomUUID().toString().substring(0, 8);
        String uniqueName = "Test Runner " + token;
        String uniqueEmail = "user_" + token + "@fitprotest.com";
        String password = "Password@2026!";

        User registered = authService.register(uniqueName, uniqueEmail, password, "MEMBER");
        Assertions.assertNotNull(registered);
        Assertions.assertEquals(uniqueName, registered.getFullName());
        Assertions.assertEquals(uniqueEmail, registered.getEmail());
        Assertions.assertEquals("MEMBER", registered.getRole());
        Assertions.assertTrue(registered.getId() > 0);

        // Test duplicate email rejection
        Assertions.assertThrows(AppException.class, () ->
                authService.register("Another Person", uniqueEmail, "anotherPassword", "MEMBER"));

        // Test login success
        User loggedIn = authService.login(uniqueEmail, password);
        Assertions.assertNotNull(loggedIn);
        Assertions.assertEquals(registered.getId(), loggedIn.getId());

        // Test login failure with bad password
        Assertions.assertThrows(AppException.class, () ->
                authService.login(uniqueEmail, "IncorrectPassword"));

        // Test login failure with nonexistent email
        Assertions.assertThrows(AppException.class, () ->
                authService.login("nonexistent@nowhere.org", "anyPass"));

        // Test login with full name / username instead of email
        User loggedInByName = authService.login(uniqueName, password);
        Assertions.assertNotNull(loggedInByName);
        Assertions.assertEquals(registered.getId(), loggedInByName.getId());
    }

    @Test
    @DisplayName("Login validation rejects blank identifier and blank password")
    public void testLoginValidationEmptyInputs() {
        AppException ex1 = Assertions.assertThrows(AppException.class, () ->
                authService.login("", "password123"));
        Assertions.assertTrue(ex1.getMessage().contains("Please enter your email address or username"));

        AppException ex2 = Assertions.assertThrows(AppException.class, () ->
                authService.login("user@example.com", ""));
        Assertions.assertTrue(ex2.getMessage().contains("Please enter your password"));
    }

    @Test
    @DisplayName("Registration prevents privilege escalation: ADMIN input forces MEMBER role")
    public void testRegistrationBlocksAdminPrivilegeEscalation() throws Exception {
        String testEmail = "fake_admin_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User user = authService.register("Hacker", testEmail, "secret123", "ADMIN");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("MEMBER", user.getRole(), "User submitting role=ADMIN must be forced to MEMBER");
        Assertions.assertFalse(user.isAdmin());
        Assertions.assertTrue(user.isMember());
    }

    @Test
    @DisplayName("Registration permits legitimate TRAINER role selection")
    public void testRegistrationAllowsTrainer() throws Exception {
        String testEmail = "coach_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        User user = authService.register("Coach Sam", testEmail, "secret123", "TRAINER");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("TRAINER", user.getRole());
        Assertions.assertTrue(user.isTrainer());
    }
}

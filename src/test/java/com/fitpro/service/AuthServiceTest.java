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
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@fitprotest.com";
        String password = "Password@2026!";

        User registered = authService.register("Test Runner", uniqueEmail, password, "MEMBER");
        Assertions.assertNotNull(registered);
        Assertions.assertEquals("Test Runner", registered.getFullName());
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
    }
}

package com.fitpro.model;

/**
 * System Administrator with full access to user management, workout content,
 * platform telemetry, and system configuration.
 * Demonstrates OOP Inheritance and Polymorphism alongside {@link Member} and {@link Trainer}.
 */
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(int id, String fullName, String email, String passwordHash) {
        super(id, fullName, email, passwordHash);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public String getWelcomeMessage() {
        return "System Administration Console — Welcome, " + getFullName();
    }
}

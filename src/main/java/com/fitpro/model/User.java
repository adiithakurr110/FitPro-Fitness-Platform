package com.fitpro.model;

/**
 * Base class for every account. Demonstrates INHERITANCE and ABSTRACTION:
 * {@link Member} and {@link Trainer} extend it and override role-specific behaviour.
 */
public abstract class User {
    private int id;
    private String fullName;
    private String email;
    private String passwordHash;

    protected User() { }

    protected User(int id, String fullName, String email, String passwordHash) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /** Role stored in the database, e.g. "MEMBER" or "TRAINER". */
    public abstract String getRole();

    /** Message shown on the dashboard - differs per role (POLYMORPHISM). */
    public abstract String getWelcomeMessage();

    /** Simple factory: builds the right subclass from the DB role string. */
    public static User of(String role) {
        return "TRAINER".equalsIgnoreCase(role) ? new Trainer() : new Member();
    }

    public boolean isTrainer() { return "TRAINER".equals(getRole()); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}

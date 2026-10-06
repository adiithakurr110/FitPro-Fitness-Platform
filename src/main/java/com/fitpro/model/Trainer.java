package com.fitpro.model;

/** A coach who publishes workout programs for members. */
public class Trainer extends User {
    @Override
    public String getRole() { return "TRAINER"; }

    @Override
    public String getWelcomeMessage() {
        return "Welcome coach " + getFullName() + ". Publish a new program for your members!";
    }
}

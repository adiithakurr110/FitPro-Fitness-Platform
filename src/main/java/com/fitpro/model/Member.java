package com.fitpro.model;

/** A person who enrolls in programs and logs training sessions. */
public class Member extends User {
    @Override
    public String getRole() { return "MEMBER"; }

    @Override
    public String getWelcomeMessage() {
        return "Ready to crush today's workout, " + getFullName() + "?";
    }
}

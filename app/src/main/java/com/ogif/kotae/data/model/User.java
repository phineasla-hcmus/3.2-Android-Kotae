package com.ogif.kotae.data.model;

public class User {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    private String job;
    private int yob;
    private String role;

    /**
     * Create user with default role as "user"
     */
    public User(String job, int yob) {
        this.job = job;
        this.yob = yob;
        this.role = "user";
    }

    public User(String job, int yob, String role) {
        this.job = job;
        this.yob = yob;
        this.role = role;
    }

    public String getJob() {
        return job;
    }

    public User setJob(String job) {
        this.job = job;
        return this;
    }

    public int getYob() {
        return yob;
    }

    public User setYob(int yob) {
        this.yob = yob;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }
}

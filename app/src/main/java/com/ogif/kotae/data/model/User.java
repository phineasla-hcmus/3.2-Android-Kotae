package com.ogif.kotae.data.model;

import com.google.firebase.firestore.DocumentId;
import com.ogif.kotae.Global;

import java.util.Date;

public class User {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    @DocumentId
    private String id;
    private String username;
    private String job;
    private int yob;
    private String role;
    private String avatar;
    private int xp;
    private int xpDaily;
    private Date xpDailyLastUpdate;
    private int report;
    private boolean blocked;

    public User() {
    }

    /**
     * Create user with default role as "user" and avatar
     */
    public User(String username, String job, int yob) {
        this.username = username;
        this.job = job;
        this.yob = yob;
        this.role = "user";
        this.avatar = Global.DEFAULT_USER_AVATAR;
    }

    public User(String username, String avatar, int report, boolean blocked) {
        this.username = username;
        this.avatar = avatar;
        this.report = report;
        this.blocked = blocked;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }


    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
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

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatarUrl) {
        this.avatar = avatar;
        return this;
    }

    public int getXp() {
        return xp;
    }

    public User setXp(int xp) {
        this.xp = xp;
        return this;
    }

    public int getXpDaily() {
        return xpDaily;
    }

    public User setXpDaily(int xpDaily) {
        this.xpDaily = xpDaily;
        return this;
    }

    public Date getXpDailyLastUpdate() {
        return xpDailyLastUpdate;
    }

    public User setXpDailyLastUpdate(Date xpDailyLastUpdate) {
        this.xpDailyLastUpdate = xpDailyLastUpdate;
        return this;
    }

    public void resetXpDaily() {
        this.xpDaily = 0;
        this.xpDailyLastUpdate = new Date();
    }
}

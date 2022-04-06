package com.ogif.kotae.data.model;

public class Device {
    String userId;
    String token;

    public Device() {
    }

    public Device(String userID, String token) {
        this.userId = userID;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

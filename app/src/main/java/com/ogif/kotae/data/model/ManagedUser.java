package com.ogif.kotae.data.model;

public class ManagedUser {
    private String userId, userName;
    private int upvote, downvote, report, answer;
    private boolean blocked;

    public ManagedUser(String userId, String userName, int upvote, int downvote, int report, int answer, boolean blocked) {
        this.userId = userId;
        this.userName = userName;
        this.upvote = upvote;
        this.downvote = downvote;
        this.report = report;
        this.answer = answer;
        this.blocked = blocked;
    }

    public ManagedUser() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}

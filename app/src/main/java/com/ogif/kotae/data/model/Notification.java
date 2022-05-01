package com.ogif.kotae.data.model;

import com.google.firebase.Timestamp;

public class Notification {
    String questionId;
    String authorId;
    Timestamp time;
    String userAction;
    String userId;

    Notification() {

    }

    public Notification(String questionId, String authorId, String userId, String userAction) {
        this.questionId = questionId;
        this.authorId = authorId;
        this.time = Timestamp.now();
        this.userAction = userAction;
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getUserAction() {
        return userAction;
    }

    public String getUserId() {
        return userId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

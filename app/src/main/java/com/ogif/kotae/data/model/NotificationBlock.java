package com.ogif.kotae.data.model;

import com.google.firebase.Timestamp;

public class NotificationBlock {
    String questionId;
    int number;
    String lastUserDoAction;
    String action;
    Timestamp timestamp;

    public NotificationBlock(String questionId, int number, String lastUserDoAction, String action, Timestamp timestamp) {
        this.questionId = questionId;
        this.number = number;
        this.lastUserDoAction = lastUserDoAction;
        this.action = action;
        this.timestamp = timestamp;
    }

    public NotificationBlock(String questionId, String lastUserDoAction, String action, Timestamp timestamp) {
        this.questionId = questionId;
        this.number = 1;
        this.lastUserDoAction = lastUserDoAction;
        this.action = action;
        this.timestamp = timestamp;
    }

    public void increaseNumberOneUnit() {
        this.number += 1;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLastUserDoAction() {
        return lastUserDoAction;
    }

    public void setLastUserDoAction(String lastUserDoAction) {
        this.lastUserDoAction = lastUserDoAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

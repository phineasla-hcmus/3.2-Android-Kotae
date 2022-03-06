package com.ogif.kotae.data.model;

public class Answer extends Post {
    private String questionId;

    public String getQuestionId() {
        return questionId;
    }

    public Post setQuestionId(String questionId) {
        this.questionId = questionId;
        return this;
    }
}

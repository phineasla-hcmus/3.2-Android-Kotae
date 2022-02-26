package com.ogif.kotae.data.model;

import java.util.Calendar;
import java.util.Date;

public class Question {
    private String content;
    private String author;
    private String post_time;
    private  int upvote;
    private int downvote;
    private int report;
    private String subject;
    private String grade;

    public Question(String author, String grade, String subject, String content, String post_time){
        this.author = author;
        this.grade = grade;
        this.content = content;
        this.subject = subject;
        this.upvote = 0;
        this.downvote = 0;
        this.report = 0;
        this.post_time = post_time;
    }
    public Question(String author, String grade, String subject, String content, String post_time, int upvote, int downvote, int report){
        this.author = author;
        this.grade = grade;
        this.content = content;
        this.subject = subject;
        this.upvote = upvote;
        this.downvote = downvote;
        this.report = report;
        this.post_time = post_time;
    }
    public String getAuthor() { return this.author;}

    public String getQuestionContent() { return this.content; }

    public Question setQuestionContent(String content){
        this.content = content;
        return this;
    }

    public Question setUpvote(int upvote) {
        this.upvote = upvote;
        return this;
    }

    public String getPost_time() {
        return post_time;
    }

    public Question setDownvote(int downvote){
        this.downvote = downvote;
        return this;
    }

    public Question setReport(int report){
        this.report = report;
        return this;
    }
}

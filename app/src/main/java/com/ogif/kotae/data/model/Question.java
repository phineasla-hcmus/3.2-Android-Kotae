package com.ogif.kotae.data.model;

import java.util.Calendar;
import java.util.Date;

public class Question {
    private String content;
    private String author;
    private Date post_time;
    private  int upvote;
    private int downvote;
    private int report;
    private String subject;
    private String grade;

    public Question(String author, String grade, String subject, String content){
        this.author = author;
        this.grade = grade;
        this.content = content;
        this.subject = subject;
        this.post_time = Calendar.getInstance().getTime();
        this.upvote = 0;
        this.downvote = 0;
        this.report = 0;
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

    public Question setDownvote(int downvote){
        this.downvote = downvote;
        return this;
    }

    public Question setReport(int report){
        this.report = report;
        return this;
    }
}

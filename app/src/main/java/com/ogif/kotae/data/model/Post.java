package com.ogif.kotae.data.model;

import java.util.Date;

public class Post {
    private String id;
    private String title;
    private String authorId;
    private String content;
    private long postTime;
    private int upvote = 0;
    private int downvote = 0;
    private int report = 0;
    private String subjectId;
    private String gradeId;

    // TODO add images

    public String getId() {
        return id;
    }

    public Post setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Post setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    public long getPostTime() {
        return postTime;
    }

    public Post setPostTime(long postTime) {
        this.postTime = postTime;
        return this;
    }

    public Post setPostTime(Date postTime) {
        this.postTime = postTime.getTime();
        return this;
    }

    public int getUpvote() {
        return upvote;
    }

    public Post setUpvote(int upvote) {
        this.upvote = upvote;
        return this;
    }

    public int getDownvote() {
        return downvote;
    }

    public Post setDownvote(int downvote) {
        this.downvote = downvote;
        return this;
    }

    public int getReport() {
        return report;
    }

    public Post setReport(int report) {
        this.report = report;
        return this;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public Post setSubjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public String getGradeId() {
        return gradeId;
    }

    public Post setGradeId(String gradeId) {
        this.gradeId = gradeId;
        return this;
    }
}

package com.ogif.kotae.data.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public final class Comment {

    public static class Builder {
        private String authorId;
        private String authorName;
        private String content;
        private String parentId;

        public Builder() {
        }

        public Comment build() {
            return new Comment(this);
        }

        /**
         * @param id   should be the same User as "name"
         * @param name should be the same User as "id"
         */
        public Builder author(String id, String name) {
            this.authorId = id;
            this.authorName = name;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder parent(String id) {
            this.parentId = id;
            return this;
        }
    }

    /**
     * Firestore
     */
    public static class Field {
        public static final String authorId = "authorId";
        public static final String authorName = "authorName";
        public static final String content = "content";
        public static final String postTime = "postTime";
        public static final String upvote = "upvote";
        public static final String downvote = "downvote";
        public static final String report = "report";
        public static final String blocked = "blocked";
        public static final String parentId = "parentId";
    }

    @DocumentId
    private String id;
    private String authorId;
    private String author;
    private String content;
    protected Date postTime;
    protected int upvote;
    protected int downvote;
    protected int report;
    protected boolean blocked;
    private String parentId;

    public Comment() {
    }

    public Comment(Builder builder) {
        this.authorId = builder.authorId;
        this.author = builder.authorName;
        this.content = builder.content;
        this.postTime = new Date();
        this.parentId = builder.parentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public int getUpvote() {
        return upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public int getReport() {
        return report;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getParentId() {
        return parentId;
    }
}

package com.ogif.kotae.data.model;

import com.google.firebase.firestore.DocumentId;

public class Vote {
    @DocumentId
    private String id;
    private String user;
    private boolean upvote;

    public Vote() {
    }

    public Vote(String user, boolean isUpvote) {
        this.user = user;
        this.upvote = isUpvote;
    }

    public static class Field {
        public static final String user = "user";
        public static final String upvote = "upvote";
    }

    public String getId() {
        return id;
    }

    public Vote setId(String id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public Vote setUser(String user) {
        this.user = user;
        return this;
    }

    public boolean isUpvote() {
        return upvote;
    }

    public boolean isDownvote() {
        return !upvote;
    }

    public Vote setVote(boolean isUpvote) {
        this.upvote = isUpvote;
        return this;
    }
}

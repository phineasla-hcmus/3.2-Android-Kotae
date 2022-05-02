package com.ogif.kotae.data.model;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Vote {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UPVOTE, NONE, DOWNVOTE})
    public @interface State {
    }

    public static final int UPVOTE = 1;
    public static final int NONE = 0;
    public static final int DOWNVOTE = -1;

    @DocumentId
    private String id;
    private String authorId;
    private String recordId;
    private boolean upvote;

    public Vote() {
    }

    public Vote(String authorId, String recordId, boolean isUpvote) {
        this.authorId = authorId;
        this.recordId = recordId;
        this.upvote = isUpvote;
    }

    public static class Field {
        public static final String authorId = "authorId";
        public static final String recordId = "recordId";
        public static final String upvote = "upvote";
    }

    public String getId() {
        return id;
    }

    public Vote setId(String id) {
        this.id = id;
        return this;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Vote setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getRecordId() {
        return recordId;
    }

    public Vote setRecordId(String recordId) {
        this.recordId = recordId;
        return this;
    }

    public boolean isUpvote() {
        return upvote;
    }

    @Exclude
    public boolean isDownvote() {
        return !upvote;
    }

    public Vote setVote(boolean isUpvote) {
        this.upvote = isUpvote;
        return this;
    }
}

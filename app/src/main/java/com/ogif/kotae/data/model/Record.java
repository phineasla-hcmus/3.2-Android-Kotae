package com.ogif.kotae.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.ogif.kotae.Global;

import org.jetbrains.annotations.TestOnly;

import java.util.Date;

/**
 * Interface for Question, Answer, Comment
 *
 * @see <a href="https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class">
 * Subclassing a builder class
 * </a>
 * @see <a href="https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/">
 * Apply builder pattern with inheritatance
 * </a>
 */
public abstract class Record {
    @DocumentId
    protected String id;
    protected String authorId;
    protected String author;
    protected String content;
    protected Date postTime;
    protected int upvote;
    protected int downvote;
    protected int report;
    protected boolean blocked;

    @Exclude
    protected String voteId;
    @Exclude
    @Vote.State
    protected int voteState;

    /**
     * Firestore
     */
    public static class Field {
        public static final String AUTHOR_ID = "authorId";
        public static final String AUTHOR_NAME = "authorName";
        public static final String CONTENT = "content";
        public static final String POST_TIME = "postTime";
        public static final String UPVOTE = "upvote";
        public static final String DOWNVOTE = "downvote";
        public static final String REPORT = "report";
        public static final String BLOCKED = "blocked";
    }

    public abstract static class Builder<T> {
        private String id;
        private String authorId;
        private String authorName;
        private String content;
        private boolean blocked;

        public Builder() {
        }

        public abstract T getThis();

        /**
         * @implNote Should only be used when fetch data from database, not when insert data into
         * database, as <a href="https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/DocumentId">
         * ID will be ignored
         * </a>
         */
        public T id(String id) {
            this.id = id;
            return getThis();
        }

        /**
         * @param id   should be the same User as "name"
         * @param name should be the same User as "id"
         */
        public T author(String id, String name) {
            this.authorId = id;
            this.authorName = name;
            return getThis();
        }

        public T content(String content) {
            this.content = content;
            return getThis();
        }

        @TestOnly
        public T block(boolean block) {
            this.blocked = block;
            return getThis();
        }
    }

    public Record() {
    }

    public Record(@NonNull Builder<?> builder) {
        this.id = builder.id;
        this.authorId = builder.authorId;
        this.author = builder.authorName;
        this.content = builder.content;
        this.blocked = builder.blocked;
        this.postTime = new Date();
    }

    @Nullable
    protected static <T extends Record> T fromDocument(@NonNull DocumentSnapshot document, @NonNull Class<T> clazz) {
        T record;
        if (!document.exists())
            return null;
        try {
            record = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        record.id = document.getId();
        record.authorId = document.getString(Record.Field.AUTHOR_ID);
        record.author = document.getString(Record.Field.AUTHOR_NAME);
        record.content = document.getString(Record.Field.CONTENT);
        record.postTime = document.getDate(Record.Field.POST_TIME);
        Integer checkNull = document.get(Record.Field.UPVOTE, int.class);
        if (checkNull != null)
            record.upvote = checkNull;
        checkNull = document.get(Record.Field.DOWNVOTE, int.class);
        if (checkNull != null)
            record.downvote = checkNull;
        checkNull = document.get(Record.Field.REPORT, int.class);
        if (checkNull != null)
            record.report = checkNull;
        Boolean blocked = document.getBoolean(Record.Field.BLOCKED);
        if (blocked != null)
            record.blocked = blocked;
        return record;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Exclude
    @Nullable
    public String getVoteId() {
        return voteId;
    }

    @Exclude
    @Vote.State
    public int getVoteState() {
        return voteState;
    }

    @Exclude
    public void setVoteState(String voteId, @Vote.State int voteState) {
        this.voteId = voteId;
        this.voteState = voteState;
    }

    @Global.Collection
    @Exclude
    public abstract String getCollectionName();
}

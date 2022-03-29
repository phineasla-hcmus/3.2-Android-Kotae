package com.ogif.kotae.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;

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
        record.authorId = document.getString(Record.Field.authorId);
        record.author = document.getString(Record.Field.authorName);
        record.content = document.getString(Record.Field.content);
        record.postTime = document.getDate(Record.Field.postTime);
        Integer checkNull = document.get(Record.Field.upvote, int.class);
        if (checkNull != null)
            record.upvote = checkNull;
        checkNull = document.get(Record.Field.downvote, int.class);
        if (checkNull != null)
            record.downvote = checkNull;
        checkNull = document.get(Record.Field.report, int.class);
        if (checkNull != null)
            record.report = checkNull;
        Boolean blocked = document.getBoolean(Record.Field.blocked);
        if (blocked != null)
            record.blocked = blocked;
        return record;
    }

    public abstract static class Builder<T extends Record.Builder<T>> {
        private String id;
        private String authorId;
        private String authorName;
        private String content;
        private boolean block;

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
            this.block = block;
            return getThis();
        }
    }

    public Record(@NonNull Builder<?> builder) {
        this.id = builder.id;
        this.authorId = builder.authorId;
        this.author = builder.authorName;
        this.content = builder.content;
        this.blocked = builder.block;
        this.postTime = new Date();
    }

    public String getId() {
        return id;
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

    public Date getRecordTime() {
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
}

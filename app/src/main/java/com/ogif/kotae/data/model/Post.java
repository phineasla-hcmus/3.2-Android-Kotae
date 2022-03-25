package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.TestOnly;

import java.util.Date;

/**
 * Interface for Question, Answer
 *
 * @see <a href="https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class">
 * Subclassing a builder class
 * </a>
 * @see <a href="https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/">
 * Apply builder pattern with inheritatance
 * </a>
 */
public abstract class Post implements Parcelable {
    public abstract static class Builder<T extends Builder<T>> {
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

    public Post() {
    }

    public Post(@NonNull Parcel parcel) {
        // Has to be exact order from writeToParcel
        id = parcel.readString();
        authorId = parcel.readString();
        author = parcel.readString();
        content = parcel.readString();
        postTime = new Date(parcel.readLong());
        upvote = parcel.readInt();
        downvote = parcel.readInt();
        report = parcel.readInt();
        blocked = parcel.readInt() == 1;
    }

    public Post(@NonNull Builder<?> builder) {
        this.id = builder.id;
        this.authorId = builder.authorId;
        this.author = builder.authorName;
        this.content = builder.content;
        this.blocked = builder.block;
        this.postTime = new Date();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // Has to be exact order from Post(Parcel parcel)
        parcel.writeString(id);
        parcel.writeString(authorId);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeLong(postTime.getTime());
        parcel.writeInt(upvote);
        parcel.writeInt(downvote);
        parcel.writeInt(report);
        parcel.writeInt(blocked ? 1 : 0);
    }

    @Nullable
    protected static <T extends Post> T fromDocument(@NonNull DocumentSnapshot document, @NonNull Class<T> clazz) {
        T post;
        if (!document.exists())
            return null;
        try {
            post = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        post.id = document.getId();
        post.authorId = document.getString(Field.authorId);
        post.author = document.getString(Field.authorName);
        post.content = document.getString(Field.content);
        post.postTime = document.getDate(Field.postTime);
        Integer checkNull = document.get(Field.upvote, int.class);
        if (checkNull != null)
            post.upvote = checkNull;
        checkNull = document.get(Field.downvote, int.class);
        if (checkNull != null)
            post.downvote = checkNull;
        checkNull = document.get(Field.report, int.class);
        if (checkNull != null)
            post.report = checkNull;
        Boolean blocked = document.getBoolean(Field.blocked);
        if (blocked != null)
            post.blocked = blocked;
        return post;
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
}

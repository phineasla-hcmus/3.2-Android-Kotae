package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

import org.jetbrains.annotations.TestOnly;

import java.util.Date;

/**
 * https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class
 * https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/
 */

public abstract class Post implements Parcelable {
    public abstract static class Builder<T extends Builder<T>> {
        private String authorId;
        private String authorName;
        private String content;
        private boolean block;

        public Builder() {
        }

        public abstract T getThis();

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

    @DocumentId
    private String id;
    private String authorId;
    private String authorName;
    private String content;
    private Date postTime;
    private int upvote;
    private int downvote;
    private int report;
    private boolean blocked;

    // TODO add images

    public Post() {
    }

    public Post(Parcel parcel) {
        // Has to be exact order from writeToParcel
        id = parcel.readString();
        authorId = parcel.readString();
        authorName = parcel.readString();
        content = parcel.readString();
        postTime = new Date(parcel.readLong());
        upvote = parcel.readInt();
        downvote = parcel.readInt();
        report = parcel.readInt();
        blocked = parcel.readInt() == 1;
    }

    public Post(Builder<?> builder) {
        this.authorId = builder.authorId;
        this.authorName = builder.authorName;
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
        parcel.writeString(authorName);
        parcel.writeString(content);
        parcel.writeLong(postTime.getTime());
        parcel.writeInt(upvote);
        parcel.writeInt(downvote);
        parcel.writeInt(report);
        parcel.writeInt(blocked ? 1 : 0);
    }

    public String getId() {
        return id;
    }

    @PropertyName("content")
    public String getContent() {
        return content;
    }

    @PropertyName("author_id")
    public String getAuthorId() {
        return authorId;
    }

    @PropertyName("author")
    public String getAuthorName() {
        return authorName;
    }

    @PropertyName("post_time")
    public Date getPostTime() {
        return postTime;
    }

    // @PropertyName("post_time")
    // public Post setPostTime(long postTime) {
    //     this.postTime = postTime;
    //     return this;
    // }
    //
    // public Post setPostTime(Date postTime) {
    //     this.postTime = postTime.getTime();
    //     return this;
    // }

    @PropertyName("upvote")
    public int getUpvote() {
        return upvote;
    }

    @PropertyName("downvote")
    public int getDownvote() {
        return downvote;
    }

    @PropertyName("report")
    public int getReport() {
        return report;
    }

    @PropertyName("blocked")
    public boolean isBlocked() {
        return blocked;
    }
}

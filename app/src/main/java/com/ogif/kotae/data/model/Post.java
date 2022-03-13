package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public abstract class Post implements Parcelable {
    private String id;
    private String title;
    private String authorId;
    private String content;
    private long postTime;
    private int upvote;
    private int downvote;
    private int report;
    private String subjectId;
    private String gradeId;
    private boolean blocked;

    // TODO add images

    public Post() {
    }

    public Post(Parcel parcel) {
        // Has to be exact order from writeToParcel
        id = parcel.readString();
        title = parcel.readString();
        authorId = parcel.readString();
        content = parcel.readString();
        postTime = parcel.readLong();
        upvote = parcel.readInt();
        downvote = parcel.readInt();
        report = parcel.readInt();
        subjectId = parcel.readString();
        gradeId = parcel.readString();
        blocked = parcel.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // Has to be exact order from Post(Parcel parcel)
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(authorId);
        parcel.writeString(content);
        parcel.writeLong(postTime);
        parcel.writeInt(upvote);
        parcel.writeInt(downvote);
        parcel.writeInt(report);
        parcel.writeString(subjectId);
        parcel.writeString(gradeId);
        parcel.writeInt(blocked ? 1 : 0);
    }

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

    public boolean isBlocked() {
        return blocked;
    }

    public Post setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }
}

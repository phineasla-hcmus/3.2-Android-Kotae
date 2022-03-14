package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public abstract class Post implements Parcelable {
    @DocumentId
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

    public Post(String id, String title, String authorId, String content, long postTime, int upvote, int downvote, int report, String subjectId, String gradeId, boolean blocked) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.content = content;
        this.postTime = postTime;
        this.upvote = upvote;
        this.downvote = downvote;
        this.report = report;
        this.subjectId = subjectId;
        this.gradeId = gradeId;
        this.blocked = blocked;
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

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    @PropertyName("content")
    public String getContent() {
        return content;
    }

    @PropertyName("content")
    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    @PropertyName("author")
    public String getAuthorId() {
        return authorId;
    }

    @PropertyName("author")
    public Post setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    @PropertyName("post_time")
    public long getPostTime() {
        return postTime;
    }

    @PropertyName("post_time")
    public Post setPostTime(long postTime) {
        this.postTime = postTime;
        return this;
    }

    public Post setPostTime(Date postTime) {
        this.postTime = postTime.getTime();
        return this;
    }

    @PropertyName("upvote")
    public int getUpvote() {
        return upvote;
    }

    @PropertyName("upvote")
    public Post setUpvote(int upvote) {
        this.upvote = upvote;
        return this;
    }

    @PropertyName("downvote")
    public int getDownvote() {
        return downvote;
    }

    @PropertyName("downvote")
    public Post setDownvote(int downvote) {
        this.downvote = downvote;
        return this;
    }

    @PropertyName("report")
    public int getReport() {
        return report;
    }

    @PropertyName("report")
    public Post setReport(int report) {
        this.report = report;
        return this;
    }

    @PropertyName("subject")
    public String getSubjectId() {
        return subjectId;
    }

    @PropertyName("subject")
    public Post setSubjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    @PropertyName("grade")
    public String getGradeId() {
        return gradeId;
    }

    @PropertyName("grade")
    public Post setGradeId(String gradeId) {
        this.gradeId = gradeId;
        return this;
    }

    @PropertyName("blocked")
    public boolean isBlocked() {
        return blocked;
    }

    @PropertyName("blocked")
    public Post setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }
}

package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer extends Post {
    private String questionId;

    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public Answer() {
        super();
    }

    public Answer(Parcel parcel) {
        super(parcel);
        questionId = parcel.readString();
    }

    public Answer(String id, String title, String authorId, String content, long postTime, int upvote, int downvote, int report, String subjectId, String gradeId, boolean blocked, String questionId) {
        super(id, title, authorId, content, postTime, upvote, downvote, report, subjectId, gradeId, blocked);
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public Post setQuestionId(String questionId) {
        this.questionId = questionId;
        return this;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(questionId);
    }
}

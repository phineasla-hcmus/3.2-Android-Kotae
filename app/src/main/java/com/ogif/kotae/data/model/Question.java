package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question extends Post {
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public Question() {
        super();
    }

    public Question(String id, String title, String authorId, String content, long postTime, int upvote, int downvote, int report, String subjectId, String gradeId, boolean blocked) {
        super(id, title, authorId, content, postTime, upvote, downvote, report, subjectId, gradeId, blocked);
    }

    public Question(Parcel parcel) {
        super(parcel);
    }
}

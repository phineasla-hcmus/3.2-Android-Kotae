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

    private String subjectId;
    private String subject;
    private String gradeId;
    private String grade;

    public Question() {
        super();
    }

    public Question(String title, String authorId, String content, long postTime, int upvote, int downvote, int report, boolean blocked, String subjectId, String gradeId) {
        super(title, authorId, content, postTime, upvote, downvote, report, blocked);
        this.subjectId = subjectId;
        this.gradeId = gradeId;
    }

    public Question(String id, String title, String authorId, String content, long postTime, int upvote, int downvote, int report, boolean blocked, String subjectId, String gradeId) {
        super(id, title, authorId, content, postTime, upvote, downvote, report, blocked);
        this.subjectId = subjectId;
        this.gradeId = gradeId;
    }

    public Question(Parcel parcel) {
        super(parcel);
        subjectId = parcel.readString();
        gradeId = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(subjectId);
        parcel.writeString(gradeId);
    }
    
    @PropertyName("subjectId")
    public String getSubjectId() {
        return subjectId;
    }

    @PropertyName("subjectId")
    public Post setSubjectId(String subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    @PropertyName("subject")
    public String getSubject() {
        return subject;
    }

    @PropertyName("gradeId")
    public String getGradeId() {
        return gradeId;
    }

    @PropertyName("gradeId")
    public Post setGradeId(String gradeId) {
        this.gradeId = gradeId;
        return this;
    }

    @PropertyName("grade")
    public String getGrade() {
        return grade;
    }
}

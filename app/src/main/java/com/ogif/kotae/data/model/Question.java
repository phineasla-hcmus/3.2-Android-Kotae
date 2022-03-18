package com.ogif.kotae.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

/**
 * https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class
 * https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/
 */

public final class Question extends Post {
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public static class Builder extends Post.Builder<Builder> {
        private String title;
        private String subjectId;
        private String subject;
        private String gradeId;
        private String grade;

        public Builder() {
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public Question build() {
            return new Question(this);
        }

        public Builder title(String title) {
            this.title = title;
            return getThis();
        }

        public Builder subject(String id, String name) {
            this.subjectId = id;
            this.subject = name;
            return getThis();
        }

        public Builder grade(String id, String name) {
            this.gradeId = id;
            this.grade = name;
            return getThis();
        }
    }

    private String title;
    private String subjectId;
    private String subject;
    private String gradeId;
    private String grade;

    public Question() {
        super();
    }

    public Question(Parcel parcel) {
        super(parcel);
        title = parcel.readString();
        subjectId = parcel.readString();
        subject = parcel.readString();
        gradeId = parcel.readString();
        grade = parcel.readString();
    }

    public Question(Builder builder) {
        super(builder);
        this.title = builder.title;
        this.subjectId = builder.subjectId;
        this.subject = builder.subject;
        this.gradeId = builder.gradeId;
        this.grade = builder.grade;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(title);
        parcel.writeString(subjectId);
        parcel.writeString(subject);
        parcel.writeString(gradeId);
        parcel.writeString(grade);
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("subjectId")
    public String getSubjectId() {
        return subjectId;
    }

    @PropertyName("subject")
    public String getSubject() {
        return subject;
    }

    @PropertyName("gradeId")
    public String getGradeId() {
        return gradeId;
    }

    @PropertyName("grade")
    public String getGrade() {
        return grade;
    }
}

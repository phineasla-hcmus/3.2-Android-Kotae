package com.ogif.kotae.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.ogif.kotae.Global;

import java.util.ArrayList;

public final class Question extends Post {
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private int answer;
    private String title;
    private String subjectId;
    private String subject;
    private String gradeId;
    private String grade;

    /**
     * Firestore
     */
    public static class Field extends Post.Field {
        public static final String ANSWER = "answer";
        public static final String TITLE = "title";
        public static final String SUBJECT_ID = "subjectId";
        public static final String SUBJECT = "subject";
        public static final String GRADE_ID = "gradeId";
        public static final String GRADE = "grade";
    }

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

    public Question() {
        super();
    }

    public Question(Builder builder) {
        super(builder);
        this.title = builder.title;
        this.subjectId = builder.subjectId;
        this.subject = builder.subject;
        this.gradeId = builder.gradeId;
        this.grade = builder.grade;
    }

    public Question(@NonNull Parcel parcel) {
        super(parcel);
        answer = parcel.readInt();
        title = parcel.readString();
        subjectId = parcel.readString();
        subject = parcel.readString();
        gradeId = parcel.readString();
        grade = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeInt(answer);
        parcel.writeString(title);
        parcel.writeString(subjectId);
        parcel.writeString(subject);
        parcel.writeString(gradeId);
        parcel.writeString(grade);
    }

    /**
     * Alternative for {@link DocumentSnapshot#toObject(Class)}
     *
     * @return Question if DocumentSnapshot.exist() == true, else null
     */
    @Nullable
    public static Question fromDocument(@NonNull DocumentSnapshot document) {
        Question question = Post.fromDocument(document, Question.class);
        if (question == null)
            return null;
        question.title = document.getString(Field.TITLE);
        question.subjectId = document.getString(Field.SUBJECT_ID);
        question.subject = document.getString(Field.SUBJECT);
        question.gradeId = document.getString(Field.GRADE_ID);
        question.grade = document.getString(Field.GRADE);
        return question;
    }

    public String getTitle() {
        return title;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public String getGradeId() {
        return gradeId;
    }

    public String getGrade() {
        return grade;
    }

    public int getAnswer() {
        return answer;
    }

    @Global.Collection
    @Exclude
    public String getCollectionName() {
        return Global.COLLECTION_QUESTION;
    }
}

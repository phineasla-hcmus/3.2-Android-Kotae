package com.ogif.kotae.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
        private List<String> imageIds;

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

        public Builder imageIds(List<String> ids) {
            this.imageIds = ids;
            return getThis();
        }
    }

    /**
     * Firestore
     */
    public static class Field extends Post.Field {
        public static final String title = "title";
        public static final String subjectId = "subjectId";
        public static final String subject = "subject";
        public static final String gradeId = "gradeId";
        public static final String grade = "grade";
        public static final String imageId = "imageIds";
    }

    private String title;
    private String subjectId;
    private String subject;
    private String gradeId;
    private String grade;
    private List<String> imageIds;

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
        imageIds = new ArrayList<>();
        parcel.readList(imageIds, String.class.getClassLoader());
    }

    public Question(Builder builder) {
        super(builder);
        this.title = builder.title;
        this.subjectId = builder.subjectId;
        this.subject = builder.subject;
        this.gradeId = builder.gradeId;
        this.grade = builder.grade;
        this.imageIds = builder.imageIds;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(title);
        parcel.writeString(subjectId);
        parcel.writeString(subject);
        parcel.writeString(gradeId);
        parcel.writeString(grade);
        parcel.writeList(imageIds);
    }

    /**
     * Alternative for {@link DocumentSnapshot#toObject(Class)}
     *
     * @return Question if DocumentSnapshot.exist() == true, else null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static Question fromDocument(@NonNull DocumentSnapshot document) {
        Question question = Post.fromDocument(document, Question.class);
        if (question == null)
            return null;
        question.title = document.getString(Field.title);
        question.subjectId = document.getString(Field.subjectId);
        question.subject = document.getString(Field.subject);
        question.gradeId = document.getString(Field.gradeId);
        question.grade = document.getString(Field.grade);
        // https://github.com/googleapis/java-firestore/issues/60
        question.imageIds = (List<String>) document.get(Field.imageId);
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

    public List<String> getImageIds() {
        return imageIds;
    }

    public String getImageId(int i) {
        return imageIds.get(i);
    }
}

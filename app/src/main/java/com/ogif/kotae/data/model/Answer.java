package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.ogif.kotae.Global;

public final class Answer extends Post {
    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    private String questionId;

    /**
     * Firestore
     */
    public static class Field extends Post.Field {
        public static final String questionId = "questionId";
        public static final String imageIds = "imageIds";
    }

    public static class Builder extends Post.Builder<Builder> {
        private String questionId;

        public Builder() {
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public Answer build() {
            return new Answer(this);
        }

        public Builder question(String id) {
            this.questionId = id;
            return getThis();
        }
    }

    public Answer() {
        super();
    }

    public Answer(Builder builder) {
        super(builder);
        this.questionId = builder.questionId;
    }

    public Answer(Parcel parcel) {
        super(parcel);
        questionId = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(questionId);
    }

    /**
     * Alternative for {@link DocumentSnapshot#toObject(Class)}
     *
     * @return Question if DocumentSnapshot.exist() == true, else null
     */
    @Nullable
    public static Answer fromDocument(@NonNull DocumentSnapshot document) {
        Answer answer = Post.fromDocument(document, Answer.class);
        if (answer == null)
            return null;
        answer.questionId = document.getString(Field.questionId);
        return answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    @Exclude
    @Override
    public String getCollectionName() {
        return Global.COLLECTION_ANSWER;
    }
}

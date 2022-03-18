package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer extends Post {
    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public static class Builder extends Post.Builder<Builder> {
        private String questionId;

        public Builder() {}

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

    private String questionId;

    public Answer() {
        super();
    }

    public Answer(Parcel parcel) {
        super(parcel);
        questionId = parcel.readString();
    }

    public Answer(Builder builder) {
        super(builder);
        this.questionId = builder.questionId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(questionId);
    }

    public String getQuestionId() {
        return questionId;
    }
}

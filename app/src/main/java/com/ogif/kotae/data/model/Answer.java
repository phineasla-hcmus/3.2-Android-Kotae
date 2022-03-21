package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public final class Answer extends Post {
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
        private List<String> imageIds;

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

        public Answer.Builder imageIds(List<String> ids) {
            this.imageIds = ids;
            return getThis();
        }
    }

    /**
     * Firestore
     */
    public static class Field extends Post.Field {
        public static final String questionId = "questionId";
        public static final String imageIds = "imageIds";
    }

    private String questionId;
    private List<String> imageIds;

    public Answer() {
        super();
    }

    public Answer(Parcel parcel) {
        super(parcel);
        questionId = parcel.readString();
        imageIds = new ArrayList<>();
        parcel.readList(imageIds, String.class.getClassLoader());
    }

    public Answer(Builder builder) {
        super(builder);
        this.questionId = builder.questionId;
        this.imageIds = builder.imageIds;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(questionId);
        parcel.writeList(imageIds);
    }

    /**
     * Alternative for {@link DocumentSnapshot#toObject(Class)}
     *
     * @return Question if DocumentSnapshot.exist() == true, else null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static Answer fromDocument(@NonNull DocumentSnapshot document) {
        Answer answer = Post.fromDocument(document, Answer.class);
        if (answer == null)
            return null;
        answer.questionId = document.getString(Field.questionId);
        // https://github.com/googleapis/java-firestore/issues/60
        answer.imageIds = (List<String>) document.get(Question.Field.imageId);
        return answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public String getImageId(int i) {
        return imageIds.get(i);
    }
}

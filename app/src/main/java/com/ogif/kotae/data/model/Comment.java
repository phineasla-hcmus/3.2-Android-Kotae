package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public final class Comment extends Post {
    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public static class Builder extends Post.Builder<Builder> {
        private String parentId;

        public Builder() {
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }

        public Builder parent(String id) {
            this.parentId = id;
            return getThis();
        }
    }

    private String parentId;

    public Comment() {
        super();
    }

    public Comment(Parcel parcel) {
        super(parcel);
        parentId = parcel.readString();
    }

    public Comment(Builder builder) {
        super(builder);
        this.parentId = builder.parentId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(parentId);
    }

    @PropertyName("parent_id")
    public String getParentId() {
        return parentId;
    }
}

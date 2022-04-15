package com.ogif.kotae.data.model;

import com.google.firebase.firestore.Exclude;
import com.ogif.kotae.Global;

public final class Comment extends Record {

    private String parentId;

    /**
     * Firestore
     */
    public static class Field {
        public static final String PARENT_ID = "parentId";
    }

    public static class Builder extends Record.Builder<Builder> {
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
            return this;
        }
    }

    public Comment() {
    }

    public Comment(Builder builder) {
        super(builder);
        this.parentId = builder.parentId;
    }

    public String getParentId() {
        return parentId;
    }

    @Exclude
    @Override
    public String getCollectionName() {
        return Global.COLLECTION_COMMENT;
    }
}

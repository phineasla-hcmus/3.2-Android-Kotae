package com.ogif.kotae.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Interface for Question, Answer
 *
 * @see <a href="https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class">
 * Subclassing a builder class
 * </a>
 * @see <a href="https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/">
 * Apply builder pattern with inheritatance
 * </a>
 */
public abstract class Post extends Record implements Parcelable {
    protected int bookmark;
    protected int comment;
    protected List<String> imageIds;

    public abstract static class Builder<T extends Builder<T>> extends Record.Builder<T> {
        private List<String> imageIds;

        public Builder() {
        }

        public abstract T getThis();

        public Builder<T> imageIds(List<String> ids) {
            this.imageIds = ids;
            return getThis();
        }
    }

    /**
     * Firestore
     */
    public static class Field extends Record.Field {
        public static final String BOOKMARK = "bookmark";
        public static final String COMMENT = "comment";
        public static final String IMAGE_IDS = "imageIds";
    }

    public Post() {
        super();
    }

    public Post(@NonNull Builder<?> builder) {
        super(builder);
        this.imageIds = builder.imageIds;
    }

    public Post(@NonNull Parcel parcel) {
        // Has to be exact order from writeToParcel
        id = parcel.readString();
        authorId = parcel.readString();
        author = parcel.readString();
        content = parcel.readString();
        postTime = new Date(parcel.readLong());
        upvote = parcel.readInt();
        downvote = parcel.readInt();
        report = parcel.readInt();
        blocked = parcel.readInt() == 1;
        bookmark = parcel.readInt();
        comment = parcel.readInt();
        imageIds = new ArrayList<>();
        parcel.readList(imageIds, String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // Has to be exact order from Post(Parcel parcel)
        parcel.writeString(id);
        parcel.writeString(authorId);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeLong(postTime.getTime());
        parcel.writeInt(upvote);
        parcel.writeInt(downvote);
        parcel.writeInt(report);
        parcel.writeInt(blocked ? 1 : 0);
        parcel.writeInt(bookmark);
        parcel.writeInt(comment);
        parcel.writeList(imageIds);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected static <T extends Record> T fromDocument(@NonNull DocumentSnapshot document, @NonNull Class<T> clazz) {
        Post post = (Post) Record.fromDocument(document, clazz);
        if (post == null)
            return null;
        Integer checkNull = document.get(Field.BOOKMARK, int.class);
        if (checkNull != null)
            post.upvote = checkNull;
        checkNull = document.get(Field.COMMENT, int.class);
        if (checkNull != null)
            post.comment = checkNull;
        // https://github.com/googleapis/java-firestore/issues/60
        post.imageIds = (List<String>) document.get(Field.IMAGE_IDS);
        return (T) post;
    }

    public int getBookmark() {
        return bookmark;
    }

    public int getComment() {
        return comment;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public String getImageId(int i) {
        return imageIds.get(i);
    }
}

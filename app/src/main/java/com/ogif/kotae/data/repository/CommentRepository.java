package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Comment;

import java.util.Date;
import java.util.List;

public class CommentRepository extends RecordRepository<Comment> {
    private static final String TAG = "CommentRepository";
    private String authorName;
    private String postId;

    public CommentRepository(String authorId, String authorName, String postId) {
        super(Global.COLLECTION_COMMENT, authorId);
        this.authorName = authorName;
        this.postId = postId;
    }

    @Nullable
    @Override
    protected Comment toObject(@NonNull DocumentSnapshot snapshot) {
        return snapshot.toObject(Comment.class);
    }

    @Override
    protected List<Comment> toObjects(@NonNull QuerySnapshot snapshots) {
        return snapshots.toObjects(Comment.class);
    }

    public Task<Comment> create(@NonNull String postId, @NonNull String authorId, @NonNull String authorName, @NonNull String content) {
        TaskCompletionSource<Comment> taskCompletionSource = new TaskCompletionSource<>();
        Comment comment = new Comment.Builder()
                .author(authorId, authorName)
                .content(content)
                .parent(postId)
                .build();
        collectionRef.add(comment)
                .addOnSuccessListener(documentReference -> {
                    comment.setId(documentReference.getId());
                    taskCompletionSource.setResult(comment);
                })
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<Comment> create(@NonNull String content) {
        return create(postId, getAuthorId(), authorName, content);
    }

    /**
     * Experimental
     */
    public Task<List<Comment>> getListByParentWithVotes(int limit, @NonNull PagingOption option) {
        Query query = collectionRef.whereEqualTo(Comment.Field.PARENT_ID, postId);
        query = option.inject(query).limit(limit);
        return getListWithVotes(query);
    }

    public Task<List<Comment>> getListByParentWithVotes(int limit) {
        Task<QuerySnapshot> query = collectionRef
                .whereEqualTo(Comment.Field.PARENT_ID, postId)
                .orderBy(Comment.Field.POST_TIME, Query.Direction.DESCENDING)
                .limit(limit)
                .get();
        return getListWithVotes(query);
    }

    public Task<List<Comment>> getListByParentWithVotesAfter(Date previousDate, int limit) {
        Task<QuerySnapshot> query = collectionRef
                .whereEqualTo(Comment.Field.PARENT_ID, postId)
                .orderBy(Comment.Field.POST_TIME, Query.Direction.DESCENDING)
                .limit(limit)
                .startAfter(previousDate)
                .get();
        return getListWithVotes(query);
    }

    // @Deprecated
    // public void getListWithVotes(int limit, @NonNull TaskListener.State<List<Comment>> callback) {
    //     getListWithVotes(limit).addOnSuccessListener(callback::onSuccess)
    //             .addOnFailureListener(callback::onFailure);
    // }

    public void setAuthor(@NonNull String authorId, @NonNull String authorName) {
        voteRepository.setAuthorId(authorId);
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }
}

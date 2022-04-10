package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private static final String TAG = "CommentRepository";
    private final FirebaseFirestore db;
    private final CollectionReference commentsRef;
    private String authorId;
    private String authorName;
    private final VoteRepository voteRepository;

    public CommentRepository(String authorId, String authorName) {
        this.db = FirebaseFirestore.getInstance();
        this.commentsRef = db.collection("comments");
        this.authorId = authorId;
        this.authorName = authorName;
        this.voteRepository = new VoteRepository(authorId);
    }

    public Task<Void> createComment(@NonNull String postId, @NonNull String authorId, @NonNull String authorName, @NonNull String content) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Comment comment = new Comment.Builder()
                .author(authorId, authorName)
                .content(content)
                .parent(postId)
                .build();
        commentsRef.add(comment)
                .addOnSuccessListener(documentReference -> taskCompletionSource.setResult(null))
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    private Task<List<Comment>> onQueryListComplete(@NonNull Task<QuerySnapshot> query) {
        TaskCompletionSource<List<Comment>> taskCompletionSource = new TaskCompletionSource<>();
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Comment> comments = new ArrayList<>(queryDocumentSnapshots.size());
            List<String> commentIds = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Comment comment = document.toObject(Comment.class);
                comments.add(comment);
                commentIds.add(comment.getId());
            }

            taskCompletionSource.setResult(comments);
        }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<List<Comment>> getList(@NonNull String postId, int limit) {
        Task<QuerySnapshot> commentQuery = commentsRef
                .whereEqualTo(Comment.Field.PARENT_ID, postId)
                .limit(limit)
                .get();
        return onQueryListComplete(commentQuery);
    }

    private void onQueryListComplete(@NonNull Task<QuerySnapshot> query, @NonNull TaskListener.State<List<Comment>> callback) {
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Comment> comments = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                comments.add(document.toObject(Comment.class));
            }
            callback.onSuccess(comments);
        }).addOnFailureListener(callback::onFailure);
    }

    public void getList(@NonNull String postId, int limit, @NonNull TaskListener.State<List<Comment>> callback) {
        Task<QuerySnapshot> query = commentsRef.whereEqualTo("parentId", postId).limit(limit).get();
        onQueryListComplete(query, callback);
    }
}

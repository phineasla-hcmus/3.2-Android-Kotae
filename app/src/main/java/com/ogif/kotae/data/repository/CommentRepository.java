package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private final FirebaseFirestore db;
    private final CollectionReference commentsRef;
    private final MutableLiveData<StateWrapper<Comment>> mutableLiveData;

    public CommentRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.commentsRef = db.collection("comments");
        this.mutableLiveData = new MutableLiveData<>();
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

    public MutableLiveData<StateWrapper<Comment>> getMutableLiveData() {
        return mutableLiveData;
    }
}

package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private final MutableLiveData<StateWrapper<Comment>> mutableLiveData;

    public CommentRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.commentsRef = db.collection("comments");
        this.mutableLiveData = new MutableLiveData<>();
    }

    public void createComment(@NonNull String postId, @NonNull String authorId, @NonNull String authorName, @NonNull String content) {
        Comment comment = new Comment.Builder()
                .author(authorId, authorName)
                .content(content)
                .parent(postId)
                .build();
        commentsRef.add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
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

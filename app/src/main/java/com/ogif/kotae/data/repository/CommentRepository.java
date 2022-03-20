package com.ogif.kotae.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CommentRepository {
    private final FirebaseFirestore db;
    private final CollectionReference commentsRef;

    public CommentRepository() {
        db = FirebaseFirestore.getInstance();
        commentsRef = db.collection("comments");
    }

    public Task<QuerySnapshot> getList(String postId, int limit) {
        return commentsRef.whereEqualTo("parent", postId).limit(limit).get();
    }
}

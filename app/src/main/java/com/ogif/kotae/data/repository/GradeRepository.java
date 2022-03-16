package com.ogif.kotae.data.repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Grade;

import java.util.ArrayList;
import java.util.List;

public class GradeRepository {
    private final FirebaseFirestore db;
    private final CollectionReference gradesRef;

    public GradeRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.gradesRef = db.collection("grade");
    }

    public Task<DocumentSnapshot> get(String id) {
        return gradesRef.document(id).get();
    }

    public Task<QuerySnapshot> getAll() {
        return gradesRef.get();
    }

    public void getAll(TaskListener.State<List<Grade>> callback) {
        // Log.d("data", "Error getting documents: ", task.getException());
        getAll().addOnSuccessListener(result -> {
            List<Grade> grades = new ArrayList<>();
            for (QueryDocumentSnapshot document : result) {
                grades.add(new Grade(document.getId(), document.getString("name")));
            }
            callback.onSuccess(grades);
        }).addOnFailureListener(callback::onFailure);
    }

    public void get(String id, TaskListener.State<Grade> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> callback.onSuccess(documentSnapshot.toObject(Grade.class)))
                .addOnFailureListener(callback::onFailure);
    }
}

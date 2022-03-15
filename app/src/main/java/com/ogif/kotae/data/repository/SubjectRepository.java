package com.ogif.kotae.data.repository;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Subject;

import java.util.ArrayList;

public class SubjectRepository {
    private final FirebaseFirestore db;
    private final CollectionReference subjectsRef;

    public SubjectRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.subjectsRef = db.collection("subjects");
    }

    public Task<DocumentSnapshot> get(String id) {
        return subjectsRef.document(id).get();
    }

    public interface SubjectCallBack {
        void subjectsList(ArrayList<Subject> subjects);
    }

    public void getAllSubjects(SubjectCallBack subjectCallBack) {
        subjectsRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Subject> subjects = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            subjects.add(new Subject(document.getId(), document.getString("name")));
                        }
                        subjectCallBack.subjectsList(subjects);
                    } else {
                        Log.d("data", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void get(String id, TaskListener<Subject> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(Subject.class));
        }).addOnFailureListener(callback::onFailure);
    }
}

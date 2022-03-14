package com.ogif.kotae.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class QuestionRepository {
    private static final String TAG = "QuestionRepository";
    private final FirebaseFirestore db;
    private final CollectionReference questionsRef;

    public QuestionRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.questionsRef = db.collection("questions");
    }

    public Task<DocumentSnapshot> get(String id) {
        return questionsRef.document(id).get();
    }

    public void get(String id, TaskListener<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(Question.class));
        }).addOnFailureListener(callback::onFailure);
    }
}

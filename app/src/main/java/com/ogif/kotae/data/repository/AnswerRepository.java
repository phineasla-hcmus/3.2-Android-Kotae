package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;

public class AnswerRepository {
    private final FirebaseFirestore db;
    private final CollectionReference answersRef;
    private final MutableLiveData<StateWrapper<Answer>> mutableLiveData;

    public AnswerRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.mutableLiveData = new MutableLiveData<>();
        this.answersRef = db.collection("answers");
    }

    public Task<DocumentSnapshot> get(@NonNull String id) {
        return answersRef.document(id).get();
    }

    public void get(@NonNull String id, TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(Question.class));
        }).addOnFailureListener(callback::onFailure);
    }

    public void createAnswer(@NonNull String content) {
        Answer answer = new Answer("sample", null, "0FDZ97sbxRf17ac07Sx260inaPR2", content, 150000000, 0, 0, 0, "s01", "g01", false, "1");
        answersRef.add(answer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("data", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("data", "Error adding document", e);
                    }
                });

    }

    public MutableLiveData<StateWrapper<Answer>> getMutableLiveData() {
        return mutableLiveData;
    }
}

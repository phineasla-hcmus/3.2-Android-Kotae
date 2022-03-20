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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AnswerRepository {
    private final FirebaseFirestore db;
    private final CollectionReference answersRef;
    private final MutableLiveData<StateWrapper<Answer>> mutableLiveData;

    public AnswerRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.answersRef = db.collection("answers");
        this.mutableLiveData = new MutableLiveData<>();
    }

    public Task<DocumentSnapshot> get(@NonNull String id) {
        return answersRef.document(id).get();
    }

    public void get(@NonNull String id, @NonNull TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(Question.class));
        }).addOnFailureListener(callback::onFailure);
    }

    public void getList(@NonNull String questionId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        answersRef.whereEqualTo("questionId", questionId)
                .limit(limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Answer> answers = new ArrayList<>(queryDocumentSnapshots.size());
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // TODO do something
                    }
                });
    }

    public void createAnswer(@NonNull String content) {
        Answer answer = new Answer.Builder().question("0FDZ97sbxRf17ac07Sx260inaPR2")
                .author("abc", "Tam Nguyen").content(content)
                .build();
        answersRef.add(answer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("data", "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
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

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
import com.ogif.kotae.data.model.Question;

public class QuestionRepository {
    private static final String TAG = "QuestionRepository";
    private final FirebaseFirestore db;
    private final CollectionReference questionsRef;
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    public QuestionRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.questionsRef = db.collection("questions");
        this.mutableLiveData = new MutableLiveData<>();
    }

    public Task<DocumentSnapshot> get(@NonNull String id) {
        return questionsRef.document(id).get();
    }

    public void get(@NonNull String id, TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> callback.onSuccess(documentSnapshot.toObject(Question.class)))
                .addOnFailureListener(callback::onFailure);
    }

    public void createQuestion(@NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {
        Question question = new Question.Builder().title(title)
                .content(content)
                .subject(subjectId, subject)
                .grade(gradeId, grade)
                .build();
        questionsRef.add(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

    public MutableLiveData<StateWrapper<Question>> getMutableLiveData() {
        return mutableLiveData;
    }
}

package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;

public class QuestionRepository extends FirestoreGenericRepository {
    private static final String TAG = "QuestionRepository";
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    public QuestionRepository() {
        super("questions");
        this.mutableLiveData = new MutableLiveData<>();
    }

    public void get(@NonNull String id, TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> callback.onSuccess(documentSnapshot.toObject(Question.class)))
                .addOnFailureListener(callback::onFailure);
    }

    public void createQuestion(@NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {
        Question mockQuestion = new Question.Builder().title(title)
                .content(content)
                .subject(subjectId, subject)
                .grade(gradeId, grade)
                .build();
        reference.add(mockQuestion)
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

    public MutableLiveData<StateWrapper<Question>> getMutableLiveData() {
        return mutableLiveData;
    }
}

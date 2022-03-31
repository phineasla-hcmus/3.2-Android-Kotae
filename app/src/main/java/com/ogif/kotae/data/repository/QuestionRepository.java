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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private static final String TAG = "QuestionRepository";
    private final FirebaseFirestore db;
    private final CollectionReference questionsRef;
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    private String orderBy = Question.Field.POST_TIME;
    private Query.Direction orderByDirection = Query.Direction.DESCENDING;

    public QuestionRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.questionsRef = db.collection("questions");
        this.mutableLiveData = new MutableLiveData<>();
    }

    private void onQueryListComplete(@NonNull Task<QuerySnapshot> query, @NonNull TaskListener.State<List<Question>> callback) {
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Question> questions = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                questions.add(document.toObject(Question.class));
            }
            callback.onSuccess(questions);
        }).addOnFailureListener(callback::onFailure);
    }

    @NonNull
    private Task<DocumentSnapshot> get(@NonNull String id) {
        return questionsRef.document(id).get();
    }

    public DocumentReference toDocumentRef(@NonNull Question question) {
        return questionsRef.document(question.getId());
    }

    public void get(@NonNull String id, @NonNull TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> callback.onSuccess(documentSnapshot.toObject(Question.class)))
                .addOnFailureListener(callback::onFailure);
    }

    public void createQuestion(@NonNull String authorId, @NonNull String authorName, @NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {

        Question question = new Question.Builder().title(title)
                .author(authorId, authorName)
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

    public Query getHomeQuestions() {
        return questionsRef.whereEqualTo("blocked", false)
                .orderBy("postTime", Query.Direction.DESCENDING)
                .limit(20);
    }

    public void searchQuestionByKeyword(@NonNull String keyword, int limit, @NonNull TaskListener.State<List<Question>> callback) {
        Task<QuerySnapshot> query = questionsRef
                .orderBy("title")
                .startAt(keyword.toUpperCase())
                .endAt(keyword.toLowerCase() + "\uf8ff")
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }
}

package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Answer.Field;
import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AnswerRepository {
    private final FirebaseFirestore db;
    private final CollectionReference answersRef;

    private String orderBy = Answer.Field.upvote;
    private Query.Direction orderByDirection = Query.Direction.DESCENDING;

    public AnswerRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.answersRef = db.collection("answers");
    }

    private Task<DocumentSnapshot> get(@NonNull String id) {
        return answersRef.document(id).get();
    }

    private void onQueryListComplete(@NonNull Task<QuerySnapshot> query, @NonNull TaskListener.State<List<Answer>> callback) {
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Answer> answers = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                answers.add(document.toObject(Answer.class));
            }
            callback.onSuccess(answers);
        }).addOnFailureListener(callback::onFailure);
    }

    public void get(@NonNull String id, @NonNull TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(Question.class));
        }).addOnFailureListener(callback::onFailure);
    }

    public void getListByQuestion(@NonNull String questionId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = answersRef.whereEqualTo(Field.questionId, questionId)
                .orderBy(orderBy, orderByDirection)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByQuestion(@NonNull String questionId, @NonNull String afterId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = answersRef.whereEqualTo(Field.questionId, questionId)
                .startAfter(afterId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByAuthor(@NonNull String authorId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = answersRef.whereEqualTo(Field.questionId, authorId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByAuthor(@NonNull String authorId, @NonNull String afterId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = answersRef.whereEqualTo(Field.questionId, authorId)
                .startAfter(afterId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void createAnswer(@NonNull Answer answer, @NonNull TaskListener.State<DocumentReference> callback) {
        answersRef.add(answer)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public AnswerRepository setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public AnswerRepository setOrderBy(String orderBy, Query.Direction direction) {
        this.orderBy = orderBy;
        this.orderByDirection = direction;
        return this;
    }

    public Query.Direction getOrderByDirection() {
        return orderByDirection;
    }

    public AnswerRepository setOrderByDirection(Query.Direction direction) {
        this.orderByDirection = direction;
        return this;
    }
}

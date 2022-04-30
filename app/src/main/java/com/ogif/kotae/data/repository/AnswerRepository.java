package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Answer.Field;

import java.util.ArrayList;
import java.util.List;

public class AnswerRepository extends RecordRepository<Answer> {
    private static final String TAG = "AnswerRepository";

    private String orderBy = Answer.Field.UPVOTE;
    private Query.Direction orderByDirection = Query.Direction.DESCENDING;

    public AnswerRepository() {
        super(Global.COLLECTION_ANSWER);
    }

    public AnswerRepository(String authorId) {
        super(Global.COLLECTION_ANSWER, authorId);
    }

    public AnswerRepository(VoteRepository voteRepository) {
        super(Global.COLLECTION_ANSWER, voteRepository);
    }

    @Nullable
    @Override
    protected Answer toObject(@NonNull DocumentSnapshot snapshot) {
        return snapshot.toObject(Answer.class);
    }

    @Override
    protected List<Answer> toObjects(@NonNull QuerySnapshot snapshots) {
        return snapshots.toObjects(Answer.class);
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

    public void get(@NonNull String id, @NonNull TaskListener.State<Answer> callback) {
        get(id).addOnSuccessListener(callback::onSuccess).addOnFailureListener(callback::onFailure);
    }

    public void getListByQuestion(@NonNull String questionId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.questionId, questionId)
                .orderBy(orderBy, orderByDirection)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByQuestion(@NonNull String questionId, @NonNull String afterId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.questionId, questionId)
                .startAfter(afterId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByAuthor(@NonNull String authorId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.questionId, authorId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByAuthor(@NonNull String authorId, @NonNull String afterId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.questionId, authorId)
                .startAfter(afterId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void createAnswer(@NonNull Answer answer, @NonNull TaskListener.State<DocumentReference> callback) {
        collectionRef.add(answer)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    public String getOrderByField() {
        return orderBy;
    }

    public AnswerRepository setOrderByField(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public AnswerRepository setOrderByField(String orderBy, Query.Direction direction) {
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

package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Question;

public class AnswerCounterRepository {
    private final FirebaseFirestore db;
    private final CollectionReference questionsRef;

    public AnswerCounterRepository() {
        db = FirebaseFirestore.getInstance();
        questionsRef = db.collection(Global.COLLECTION_QUESTION);
    }

    public WriteBatch increment(@NonNull WriteBatch batch, @NonNull String questionId) {
        DocumentReference questionRef = questionsRef.document(questionId);
        return batch.update(questionRef, Question.Field.ANSWER, FieldValue.increment(1));
    }

    public Transaction increment(@NonNull Transaction transaction, @NonNull String questionId) {
        DocumentReference questionRef = questionsRef.document(questionId);
        return transaction.update(questionRef, Question.Field.ANSWER, FieldValue.increment(1));
    }

    public Task<Void> increment(@NonNull String questionId) {
        return questionsRef.document(questionId)
                .update(Question.Field.ANSWER, FieldValue.increment(1));
    }
}

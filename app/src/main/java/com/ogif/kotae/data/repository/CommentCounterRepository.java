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

public class CommentCounterRepository {
    private final FirebaseFirestore db;
    private final CollectionReference postsRef;

    public CommentCounterRepository(@Global.Collection String collectionPath) {
        db = FirebaseFirestore.getInstance();
        postsRef = db.collection(collectionPath);
    }

    public WriteBatch increment(@NonNull WriteBatch batch, @NonNull String postId) {
        DocumentReference questionRef = postsRef.document(postId);
        return batch.update(questionRef, Question.Field.ANSWER, FieldValue.increment(1));
    }

    public Transaction increment(@NonNull Transaction transaction, @NonNull String postId) {
        DocumentReference questionRef = postsRef.document(postId);
        return transaction.update(questionRef, Question.Field.ANSWER, FieldValue.increment(1));
    }

    public Task<Void> increment(@NonNull String postId) {
        return postsRef.document(postId).update(Question.Field.ANSWER, FieldValue.increment(1));
    }
}

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
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;

public class CommentCounterRepository {
    private final FirebaseFirestore db;
    private final DocumentReference postRef;

    public CommentCounterRepository(Post parent) {
        db = FirebaseFirestore.getInstance();
        postRef = db.collection(parent.getCollectionName()).document(parent.getId());
    }

    public WriteBatch increment(@NonNull WriteBatch batch) {
        return batch.update(postRef, Question.Field.COMMENT, FieldValue.increment(1));
    }

    public Transaction increment(@NonNull Transaction transaction) {
        return transaction.update(postRef, Question.Field.COMMENT, FieldValue.increment(1));
    }

    public Task<Void> increment() {
        return postRef.update(Question.Field.COMMENT, FieldValue.increment(1));
    }
}

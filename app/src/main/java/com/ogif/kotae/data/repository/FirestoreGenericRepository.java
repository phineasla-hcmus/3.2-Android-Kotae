package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public abstract class FirestoreGenericRepository {
    protected final FirebaseFirestore db;
    protected final CollectionReference reference;

    protected FirestoreGenericRepository(@NonNull String collectionPath) {
        this.db = FirebaseFirestore.getInstance();
        this.reference = db.collection(collectionPath);
    }

    public Task<DocumentSnapshot> get(String id) {
        return reference.document(id).get();
    }

    public Task<QuerySnapshot> getAll() {
        return reference.get();
    }

    public Query getListFrom(Object startAtId, long limit) {
        return reference.startAt(startAtId).limit(limit);
    }

    public Query getListFrom(Object startAtId, Object endAtId) {
        return reference.startAt(startAtId).endAt(endAtId);
    }

    public Query getListFrom(DocumentReference startAt, long limit) {
        return reference.startAt(startAt).limit(limit);
    }

    public Query getListFrom(DocumentReference startAt, DocumentReference endAt) {
        return reference.startAt(startAt).endAt(endAt);
    }

    public Query getListAfter(Object startAfterId, long limit) {
        return reference.startAfter(startAfterId).limit(limit);
    }

    public Query getListAfter(DocumentReference startAfter, long limit) {
        return reference.startAfter(startAfter).limit(limit);
    }
}

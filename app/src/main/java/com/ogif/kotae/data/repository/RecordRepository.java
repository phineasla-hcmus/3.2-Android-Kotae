package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.model.Record;

import java.util.ArrayList;
import java.util.List;

public abstract class RecordRepository<T extends Record> {
    protected final FirebaseFirestore db;
    protected final CollectionReference collectionReference;
    protected final VoteRepository voteRepository;

    public RecordRepository(String collectionPath, String authorId) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection(collectionPath);
        this.voteRepository = new VoteRepository(authorId);
    }

    public RecordRepository(String collectionPath, VoteRepository voteRepository) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection(collectionPath);
        this.voteRepository = voteRepository;
    }

    @Nullable
    protected abstract T toObject(DocumentSnapshot snapshot);

    protected abstract List<T> toObjects(QuerySnapshot snapshots);

    public Task<DocumentSnapshot> getDocumentSnapshot(@NonNull String id) {
        return collectionReference.document(id).get();
    }

    public Task<T> get(@NonNull String id) {
        TaskCompletionSource<T> taskCompletionSource = new TaskCompletionSource<>();
        getDocumentSnapshot(id).addOnSuccessListener(snapshot -> taskCompletionSource.setResult(toObject(snapshot)))
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<T> getWithVote(@NonNull String id) {
        TaskCompletionSource<T> taskCompletionSource = new TaskCompletionSource<>();
        getDocumentSnapshot(id).addOnSuccessListener(snapshot -> {

        }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<List<T>> getList(Task<QuerySnapshot> task) {
        TaskCompletionSource<List<T>> taskCompletionSource = new TaskCompletionSource<>();
        task.addOnSuccessListener(snapshots -> taskCompletionSource.setResult(toObjects(snapshots)))
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<List<T>> getList(Query query) {
        return getList(query.get());
    }

    // public Task<List<T>> getListWithVotes
}

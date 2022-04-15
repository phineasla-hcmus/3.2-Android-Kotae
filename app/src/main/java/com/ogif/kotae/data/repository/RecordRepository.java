package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.model.Vote;

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
    protected abstract T toObject(@NonNull DocumentSnapshot snapshot);

    protected abstract List<T> toObjects(@NonNull QuerySnapshot snapshots);

    protected Task<DocumentSnapshot> getDocumentSnapshot(@NonNull String id) {
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

    protected Task<List<T>> getList(@NonNull Task<QuerySnapshot> task) {
        TaskCompletionSource<List<T>> taskCompletionSource = new TaskCompletionSource<>();
        task.addOnSuccessListener(snapshots -> taskCompletionSource.setResult(toObjects(snapshots)))
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    protected Task<List<T>> getList(@NonNull Query query) {
        return getList(query.get());
    }

    protected Task<List<T>> getListWithVotes(@NonNull Task<QuerySnapshot> task) {
        TaskCompletionSource<List<T>> taskCompletionSource = new TaskCompletionSource<>();
        getList(task).addOnSuccessListener(records -> {
            List<String> recordIds = new ArrayList<>();
            for (T record : records) {
                recordIds.add(record.getId());
            }
            voteRepository.getList(recordIds).addOnSuccessListener(map -> {
                // Map all Vote objects to Record objects
                records.replaceAll(record -> {
                    Vote vote = map.get(record.getId());
                    if (vote != null) {
                        record.setVoteState(vote.getId(), vote.isUpvote() ?
                                Vote.UPVOTE :
                                Vote.DOWNVOTE);
                    }
                    return record;
                });
                taskCompletionSource.setResult(records);
            }).addOnFailureListener(taskCompletionSource::setException);
        }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    protected Task<List<T>> getListWithVotes(@NonNull Query query) {
        return getListWithVotes(query.get());
    }

    public void setAuthorId(@NonNull String authorId) {
        voteRepository.setAuthorId(authorId);
    }

    public String getAuthorId() {
        return voteRepository.getAuthorId();
    }
}

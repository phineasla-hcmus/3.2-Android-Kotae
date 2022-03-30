package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Vote;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;

public class VoteRepository {
    private final FirebaseFirestore db;
    private final CollectionReference votesRef;

    private String userId;

    public VoteRepository(String userId) {
        this.db = FirebaseFirestore.getInstance();
        this.votesRef = db.collection("votes");
        this.userId = userId;
    }

    public void getDocumentSnapshot(@NonNull String authorId, @NonNull String recordId, @NonNull TaskListener.State<@Nullable DocumentSnapshot> callback) {
        votesRef.whereEqualTo(Vote.Field.authorId, authorId)
                .whereEqualTo(Vote.Field.recordId, recordId)
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (snapshots.isEmpty())
                        callback.onSuccess(null);
                    else callback.onSuccess(snapshots.getDocuments().get(0));
                }).addOnFailureListener(callback::onFailure);
    }

    public void getDocumentSnapshot(@NonNull String recordId, @NonNull TaskListener.State<@Nullable DocumentSnapshot> callback) {
        getDocumentSnapshot(userId, recordId, callback);
    }

    public void get(@NonNull String authorId, @NonNull String recordId, @NonNull TaskListener.State<@Nullable Vote> callback) {
        getDocumentSnapshot(authorId, recordId, new TaskListener.State<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot result) {
                callback.onSuccess(result.toObject(Vote.class));
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void get(@NonNull String recordId, @NonNull TaskListener.State<@Nullable Vote> callback) {
        get(userId, recordId, callback);
    }

    public void set(@NonNull String authorId, @NonNull String recordId, @Vote.State int state, TaskListener.State<Void> callback) {
        if (state != Vote.NONE)
            votesRef.add(new Vote().setAuthorId(authorId)
                    .setRecordId(recordId)
                    .setVote(state == Vote.UPVOTE))
                    .addOnSuccessListener(documentReference -> {
                        if (callback != null) callback.onSuccess(null);
                    }).addOnFailureListener(callback::onFailure);
        else
            getDocumentSnapshot(recordId, new TaskListener.State<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot result) {
                    votesRef.document(result.getId()).delete().addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess(null);
                    }).addOnFailureListener(callback::onFailure);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    if (callback != null)
                        callback.onFailure(e);
                }
            });
    }

    public void set(@NonNull String recordId, @Vote.State int state, TaskListener.State<Void> callback) {
        set(userId, recordId, state, callback);
    }

    /**
     * For update or delete existing vote
     */
    public void set(@NonNull Vote existingVote, @Vote.State int newState, TaskListener.State<Void> callback) {
        DocumentReference ref = votesRef.document(existingVote.getId());
        Task<Void> result = newState != Vote.NONE
                ? ref.update(Vote.Field.upvote, newState == Vote.UPVOTE)
                : ref.delete();
        result.addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

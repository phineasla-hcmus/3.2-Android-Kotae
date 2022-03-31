package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * @param recordIds if size() > 10, it will be processed in batches
     * @param callback  return type is a map of record ID and Vote that is in either state: {@link
     *                  Vote#UPVOTE}
     *                  or {@link Vote#DOWNVOTE}. Caller should use {@link Map#getOrDefault(Object,
     *                  Object)}
     */
    public void getList(@NonNull String authorId, @NonNull List<String> recordIds, @NonNull TaskListener.State<Map<String, Vote>> callback) {
        List<List<String>> batches = Lists.partition(recordIds, 10);
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (List<String> batch : batches) {
            tasks.add(votesRef.whereEqualTo(Vote.Field.authorId, authorId)
                    .whereIn(Vote.Field.recordId, batch).get());
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener((List<Object> objects) -> {
            for (Object object : objects) {
                QuerySnapshot snapshots = (QuerySnapshot) object;
                Map<String, Vote> result = new HashMap<>();
                for (DocumentSnapshot snapshot : snapshots) {
                    Vote vote = snapshot.toObject(Vote.class);
                    if (vote != null)
                        result.put(vote.getRecordId(), vote);
                }
            }
        }).addOnFailureListener(callback::onFailure);
    }

    /**
     * @param recordIds if {@code size() > 10}, it will be processed in batches
     * @param callback  return type is a map of record ID and Vote that is in either state: {@link
     *                  Vote#UPVOTE}
     *                  or {@link Vote#DOWNVOTE}. Caller should use {@link Map#getOrDefault(Object,
     *                  Object)}
     */
    public void getList(@NonNull List<String> recordIds, @NonNull TaskListener.State<Map<String, Vote>> callback) {
        getList(userId, recordIds, callback);
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

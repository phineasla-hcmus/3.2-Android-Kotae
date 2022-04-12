package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    private String authorId;

    public VoteRepository(String authorId) {
        this.db = FirebaseFirestore.getInstance();
        this.votesRef = db.collection("votes");
        this.authorId = authorId;
    }

    public Task<Vote> get(@NonNull String authorId, @NonNull String recordId) {
        TaskCompletionSource<Vote> taskCompletionSource = new TaskCompletionSource<>();
        votesRef.whereEqualTo(Vote.Field.authorId, authorId)
                .whereEqualTo(Vote.Field.recordId, recordId)
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (snapshots.isEmpty())
                        taskCompletionSource.setResult(null);
                    else {
                        Vote vote = snapshots.getDocuments().get(0).toObject(Vote.class);
                        taskCompletionSource.setResult(vote);
                    }
                }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<Vote> get(@NonNull String recordId) {
        return get(authorId, recordId);
    }

    @Deprecated
    public void get(@NonNull String authorId, @NonNull String recordId, @NonNull TaskListener.State<@Nullable Vote> callback) {
        get(authorId, recordId).addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    @Deprecated
    public void get(@NonNull String recordId, @NonNull TaskListener.State<@Nullable Vote> callback) {
        get(authorId, recordId, callback);
    }

    /**
     * @param recordIds if <code>size() > 10</code>, it will be processed in batches
     * @return return type is a map of record ID and Vote that is in either state:
     * {@link Vote#UPVOTE} or {@link Vote#DOWNVOTE}. Caller should use
     * {@link Map#getOrDefault(Object, Object)} as {@link Vote#NONE} are not exist in the map.
     */
    public Task<Map<String, Vote>> getList(@NonNull String authorId, @NonNull List<String> recordIds) {
        TaskCompletionSource<Map<String, Vote>> taskCompletionSource = new TaskCompletionSource<>();
        List<List<String>> groups = Lists.partition(recordIds, 10);
        List<Task<QuerySnapshot>> queryTasks = new ArrayList<>();
        for (List<String> batch : groups) {
            queryTasks.add(votesRef.whereEqualTo(Vote.Field.authorId, authorId)
                    .whereIn(Vote.Field.recordId, batch).get());
        }
        Tasks.whenAllSuccess(queryTasks).addOnSuccessListener((List<Object> objectGroups) -> {
            Map<String, Vote> result = new HashMap<>();
            // Each objectGroup is the result of a single queryTask
            for (Object objectGroup : objectGroups) {
                QuerySnapshot snapshots = (QuerySnapshot) objectGroup;
                for (QueryDocumentSnapshot snapshot : snapshots) {
                    Vote vote = snapshot.toObject(Vote.class);
                    result.put(vote.getRecordId(), vote);
                }
            }
            taskCompletionSource.setResult(result);
        }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    /**
     * @param recordIds if <code>size() > 10</code>, it will be processed in batches
     * @return return type is a map of record ID and Vote that is in either state:
     * {@link Vote#UPVOTE} or {@link Vote#DOWNVOTE}. Caller should use
     * {@link Map#getOrDefault(Object, Object)} as {@link Vote#NONE} are not exist in the map.
     */
    public Task<Map<String, Vote>> getList(@NonNull List<String> recordIds) {
        return getList(authorId, recordIds);
    }

    /**
     * @param recordIds if <code>size() > 10</code>, it will be processed in batches
     * @param callback  return type is a map of record ID and Vote that is in either state:
     *                  {@link Vote#UPVOTE} or {@link Vote#DOWNVOTE}.
     *                  Caller should use {@link Map#getOrDefault(Object, Object)} as
     *                  {@link Vote#NONE} are not present.
     */
    @Deprecated
    public void getList(@NonNull String authorId, @NonNull List<String> recordIds, @NonNull TaskListener.State<Map<String, Vote>> callback) {
        getList(authorId, recordIds).addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * @param recordIds if <code>size() > 10</code>, it will be processed in batches
     * @param callback  return type is a map of record ID and Vote that is in either state: {@link
     *                  Vote#UPVOTE}
     *                  or {@link Vote#DOWNVOTE}. Caller should use {@link Map#getOrDefault(Object,
     *                  Object)}
     */
    @Deprecated
    public void getList(@NonNull List<String> recordIds, @NonNull TaskListener.State<Map<String, Vote>> callback) {
        getList(authorId, recordIds, callback);
    }

    /**
     * The task will success even recordId doesn't exist.
     * For create, update or delete vote,
     * use {@link VoteRepository#set(Vote, int, TaskListener.State)} if you can for update or delete
     * existing vote
     */
    public Task<Void> set(@NonNull String authorId, @NonNull String recordId, @Vote.State int state) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        if (state != Vote.NONE)
            votesRef.add(new Vote().setAuthorId(authorId)
                    .setRecordId(recordId)
                    .setVote(state == Vote.UPVOTE))
                    .addOnSuccessListener(documentReference -> taskCompletionSource.setResult(null))
                    .addOnFailureListener(taskCompletionSource::setException);
        else {
            get(authorId, recordId).addOnSuccessListener(vote -> {
                if (vote == null) {
                    taskCompletionSource.setResult(null);
                    return;
                }
                votesRef.document(vote.getId())
                        .delete()
                        .addOnSuccessListener(taskCompletionSource::setResult)
                        .addOnFailureListener(taskCompletionSource::setException);
            }).addOnFailureListener(taskCompletionSource::setException);
        }
        return taskCompletionSource.getTask();
    }

    /**
     * For create, update or delete vote, authorId is used from
     * {@link VoteRepository#setAuthorId(String)} use
     * {@link VoteRepository#set(Vote, int, TaskListener.State)} if you can to update or delete
     * existing vote
     */
    public Task<Void> set(@NonNull String recordId, @Vote.State int state) {
        return set(authorId, recordId, state);
    }

    /**
     * For update or delete existing vote
     */
    public Task<Void> set(@NonNull Vote existingVote, @Vote.State int newState) {
        DocumentReference ref = votesRef.document(existingVote.getId());
        return newState != Vote.NONE
                ? ref.update(Vote.Field.upvote, newState == Vote.UPVOTE)
                : ref.delete();
    }

    /**
     * For create, update or delete vote,
     * use {@link VoteRepository#set(Vote, int, TaskListener.State)} if you can for update or delete
     * existing vote
     */
    @Deprecated
    public void set(@NonNull String authorId, @NonNull String recordId, @Vote.State int state, @Nullable TaskListener.State<Void> callback) {
        if (state != Vote.NONE)
            votesRef.add(new Vote().setAuthorId(authorId)
                    .setRecordId(recordId)
                    .setVote(state == Vote.UPVOTE))
                    .addOnSuccessListener(documentReference -> {
                        if (callback != null) callback.onSuccess(null);
                    })
                    .addOnFailureListener(e -> {
                        if (callback != null) callback.onFailure(e);
                    });
        else
            get(recordId, new TaskListener.State<Vote>() {
                @Override
                public void onSuccess(Vote result) {
                    votesRef.document(result.getId()).delete().addOnSuccessListener(aVoid -> {
                        if (callback != null) callback.onSuccess(null);
                    }).addOnFailureListener(callback::onFailure);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    if (callback != null) callback.onFailure(e);
                }
            });
    }

    /**
     * For create, update or delete vote, authorId is used from {@link
     * VoteRepository#setAuthorId(String)}
     * use {@link VoteRepository#set(Vote, int, TaskListener.State)} if you can for update or
     * delete
     * existing vote
     */
    @Deprecated
    public void set(@NonNull String recordId, @Vote.State int state, @Nullable TaskListener.State<Void> callback) {
        set(authorId, recordId, state, callback);
    }

    /**
     * For update or delete existing vote
     */
    @Deprecated
    public void set(@NonNull Vote existingVote, @Vote.State int newState, @Nullable TaskListener.State<Void> callback) {
        DocumentReference ref = votesRef.document(existingVote.getId());
        Task<Void> result = newState != Vote.NONE
                ? ref.update(Vote.Field.upvote, newState == Vote.UPVOTE)
                : ref.delete();
        if (callback != null)
            result.addOnSuccessListener(callback::onSuccess)
                    .addOnFailureListener(callback::onFailure);
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}

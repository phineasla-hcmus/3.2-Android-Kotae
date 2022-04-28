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

    public Task<@Nullable DocumentSnapshot> getDocumentSnapshot(@NonNull String authorId, @NonNull String recordId) {
        TaskCompletionSource<DocumentSnapshot> taskCompletionSource = new TaskCompletionSource<>();
        votesRef.whereEqualTo(Vote.Field.authorId, authorId)
                .whereEqualTo(Vote.Field.recordId, recordId)
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (snapshots.isEmpty())
                        taskCompletionSource.setResult(null);
                    else {
                        DocumentSnapshot vote = snapshots.getDocuments().get(0);
                        taskCompletionSource.setResult(vote);
                    }
                })
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<@Nullable Vote> get(@NonNull String authorId, @NonNull String recordId) {
        TaskCompletionSource<Vote> taskCompletionSource = new TaskCompletionSource<>();
        getDocumentSnapshot(authorId, recordId).addOnSuccessListener(snapshot -> {
            taskCompletionSource.setResult(snapshot == null ? null : snapshot.toObject(Vote.class));
        }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<@Nullable Vote> get(@NonNull String recordId) {
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
     * Create create, update or delete vote.
     * The task will success even recordId or authorId doesn't exist.
     *
     * @return voteId
     */
    public Task<String> create(@NonNull String authorId, @NonNull String recordId, boolean isUpvote) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        votesRef.add(new Vote().setAuthorId(authorId).setRecordId(recordId).setVote(isUpvote))
                .addOnSuccessListener(documentReference -> {
                    taskCompletionSource.setResult(documentReference.getId());
                })
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    /**
     * Create vote
     * The task will success even recordId doesn't exist.
     * authorId is used from {@link VoteRepository#setAuthorId(String)}.
     *
     * @return voteId
     */
    public Task<String> create(@NonNull String recordId, boolean isUpvote) {
        return create(authorId, recordId, isUpvote);
    }

    public Task<Void> deleteById(@NonNull String voteId) {
        return votesRef.document(voteId).delete();
    }

    public Task<Void> delete(@NonNull String authorId, @NonNull String recordId) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        getDocumentSnapshot(authorId, recordId).addOnSuccessListener(snapshot -> {
            if (snapshot == null) {
                taskCompletionSource.setResult(null);
                return;
            }
            deleteById(snapshot.getId()).addOnSuccessListener(taskCompletionSource::setResult)
                    .addOnFailureListener(taskCompletionSource::setException);
        });
        return taskCompletionSource.getTask();
    }

    public Task<Void> delete(@NonNull String recordId) {
        return delete(authorId, recordId);
    }

    public Task<String> shift(@NonNull String voteId, boolean isUpvoteToDownvote) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        DocumentReference ref = votesRef.document(voteId);
        ref.update(Vote.Field.upvote, isUpvoteToDownvote)
                .addOnSuccessListener(unused -> taskCompletionSource.setResult(voteId))
                .addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}

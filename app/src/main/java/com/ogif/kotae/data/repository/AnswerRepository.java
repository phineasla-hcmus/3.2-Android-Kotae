package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Answer.Field;
import com.ogif.kotae.utils.repository.QueryOption;

import java.util.ArrayList;
import java.util.List;

public class AnswerRepository extends RecordRepository<Answer> {
    private static final String TAG = "AnswerRepository";

    public AnswerRepository() {
        super(Global.COLLECTION_ANSWER);
    }

    public AnswerRepository(String authorId) {
        super(Global.COLLECTION_ANSWER, authorId);
    }

    public AnswerRepository(VoteRepository voteRepository) {
        super(Global.COLLECTION_ANSWER, voteRepository);
    }

    @Nullable
    @Override
    protected Answer toObject(@NonNull DocumentSnapshot snapshot) {
        return snapshot.toObject(Answer.class);
    }

    @Override
    protected List<Answer> toObjects(@NonNull QuerySnapshot snapshots) {
        return snapshots.toObjects(Answer.class);
    }

    /**
     * Experimental
     */
    public Task<List<Answer>> getListByQuestionWithVote(@NonNull String questionId, int limit, @NonNull QueryOption option) {
        Query query = collectionRef.whereEqualTo(Field.QUESTION_ID, questionId);
        query = option.inject(query).limit(limit);
        return getListWithVotes(query);
    }

    public Task<List<Answer>> getListByQuestionWithVote(@NonNull String questionId, int limit) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.QUESTION_ID, questionId)
                .orderBy(Field.UPVOTE, Query.Direction.DESCENDING)
                .orderBy(Field.DOWNVOTE).limit(limit).get();
        return getListWithVotes(query);
    }

    public Task<List<Answer>> getListByQuestionWithVoteAfter(@NonNull String questionId, int previousUpvote, int previousDownvote, String previousId, int limit) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.QUESTION_ID, questionId)
                .orderBy(Field.UPVOTE, Query.Direction.DESCENDING)
                .orderBy(Field.DOWNVOTE)
                .startAfter(previousUpvote, previousDownvote, previousId)
                .limit(limit)
                .get();
        return getListWithVotes(query);
    }

    private void onQueryListComplete(@NonNull Task<QuerySnapshot> query, @NonNull TaskListener.State<List<Answer>> callback) {
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Answer> answers = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                answers.add(document.toObject(Answer.class));
            }
            callback.onSuccess(answers);
        }).addOnFailureListener(callback::onFailure);
    }

    // public void getListByQuestion(@NonNull String questionId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
    //     Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.QUESTION_ID, questionId)
    //             .orderBy(Field.UPVOTE, Query.Direction.DESCENDING)
    //             .limit(limit)
    //             .get();
    //     onQueryListComplete(query, callback);
    // }

    public void getListByAuthor(@NonNull String authorId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.AUTHOR_ID, authorId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getListByAuthorAfter(@NonNull String authorId, @NonNull String afterId, int limit, @NonNull TaskListener.State<List<Answer>> callback) {
        Task<QuerySnapshot> query = collectionRef.whereEqualTo(Field.AUTHOR_ID, authorId)
                .startAfter(afterId)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void createAnswer(@NonNull Answer answer, @NonNull TaskListener.State<DocumentReference> callback) {
        collectionRef.add(answer)
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }
}

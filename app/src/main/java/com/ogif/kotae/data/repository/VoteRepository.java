package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Vote;

import java.util.HashMap;
import java.util.Map;

public class VoteRepository {
    public static final int UPVOTE = 1;
    public static final int NONE = 0;
    public static final int DOWNVOTE = 1;
    private final FirebaseFirestore db;
    private final CollectionReference votesRef;

    private String authorId;

    public VoteRepository(String authorId) {
        this.db = FirebaseFirestore.getInstance();
        this.votesRef = db.collection("votes");
        this.authorId = authorId;
    }

    /**
     * @param id   Question, Answer or Comment ID
     * @param type {@link VoteRepository#UPVOTE}, {@link VoteRepository#DOWNVOTE} or
     *             {@link VoteRepository#NONE}
     */
    public void set(@NonNull String id, int type, TaskListener.State<Void> callback) {
        if (type == NONE)
            votesRef.document(id)
                    .delete()
                    .addOnSuccessListener(callback::onSuccess)
                    .addOnFailureListener(callback::onFailure);
        else votesRef.document(id)
                .set(new Vote(authorId, type == UPVOTE))
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }
}

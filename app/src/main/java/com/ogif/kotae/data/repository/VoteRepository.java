package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Vote;

import java.util.List;

public class VoteRepository {
    private final FirebaseFirestore db;
    private final CollectionReference votesRef;

    private String authorId;

    public VoteRepository(String authorId) {
        this.db = FirebaseFirestore.getInstance();
        this.votesRef = db.collection("votes");
        this.authorId = authorId;
    }

    public void getList(@NonNull List<String> ids, TaskListener.State<List<Vote>> callback) {
        
    }

    public Task<Void> set(@NonNull String id, @Vote.State int state) {
        return state == Vote.NONE
                ? votesRef.document(id).delete()
                : votesRef.document(id).set(new Vote(authorId, state == Vote.UPVOTE));
    }

    public void set(@NonNull DocumentReference doc, @Vote.State int state, TaskListener.State<Void> callback) {
        // DocumentReference voteRef = votesRef.document(doc.getId());
        // Task<Void> query;
        // query = state == NONE
        //         ? votesRef.document(id).delete()
        //         : votesRef.document(id).set(new Vote(authorId, state == UPVOTE));
        // db.runTransaction(new Transaction.Function<Void>() {
        //     @Nullable
        //     @Override
        //     public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
        //         transaction.update(doc, Post.Field.upvote, FieldValue.increment(1));
        //     }
        // });
    }


}

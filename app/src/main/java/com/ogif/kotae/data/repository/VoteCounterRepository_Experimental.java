package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.utils.DateUtils;

import java.util.Date;

public class VoteCounterRepository_Experimental {
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;
    private final VoteRepository voteRepository;

    public VoteCounterRepository_Experimental(VoteRepository voteRepository) {
        this.db = FirebaseFirestore.getInstance();
        this.usersRef = db.collection(Global.COLLECTION_USER);
        this.voteRepository = voteRepository;
    }

    private WriteBatch update(@NonNull WriteBatch batch, @NonNull Record record, boolean isUpvote, int value) {
        CollectionReference recordsRef = db.collection(record.getCollectionName());
        DocumentReference recordRef = recordsRef.document(record.getId());
        batch.update(recordRef, isUpvote ?
                Record.Field.UPVOTE :
                Record.Field.DOWNVOTE, FieldValue.increment(value));
        return batch;
    }

    private Transaction updateWithAuthorXp(@NonNull Transaction transaction, @NonNull Record record, boolean isUpvote, int value) throws FirebaseFirestoreException {
        CollectionReference recordsRef = db.collection(record.getCollectionName());
        DocumentReference recordRef = recordsRef.document(record.getId());
        DocumentReference userRef = usersRef.document(record.getAuthorId());
        DocumentSnapshot snapshot = transaction.get(userRef);
        transaction.update(recordRef, isUpvote
                ? Record.Field.UPVOTE
                : Record.Field.DOWNVOTE, FieldValue.increment(value));

        long changeInXp = isUpvote ? value : -1 * value;
        Long xpDaily = snapshot.getLong(User.Field.XP_DAILY);
        Date startOfDay = DateUtils.startOfDay();
        Date xpDailyLastUpdate = snapshot.getDate(User.Field.XP_DAILY_LAST_UPDATE);

        if (xpDaily == null || xpDailyLastUpdate == null || xpDailyLastUpdate.before(startOfDay))
            xpDaily = changeInXp;
        else xpDaily += changeInXp;

        return transaction.update(userRef, User.Field.XP, FieldValue.increment(changeInXp),
                User.Field.XP_DAILY, xpDaily,
                User.Field.XP_DAILY_LAST_UPDATE, new Date());
    }

    public WriteBatch increment(@NonNull WriteBatch batch, @NonNull Comment c, boolean isUpvote) {
        return update(batch, c, isUpvote, 1);
    }

    public WriteBatch decrement(@NonNull WriteBatch batch, @NonNull Comment c, boolean isUpvote) {
        return update(batch, c, isUpvote, -1);
    }

    public Transaction increment(@NonNull Transaction transaction, @NonNull Post p, boolean isUpvote) throws FirebaseFirestoreException {
        return updateWithAuthorXp(transaction, p, isUpvote, 1);
    }

    public Transaction decrement(@NonNull Transaction transaction, @NonNull Post p, boolean isUpvote) throws FirebaseFirestoreException {
        return updateWithAuthorXp(transaction, p, isUpvote, -1);
    }
}

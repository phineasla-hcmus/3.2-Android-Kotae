package com.ogif.kotae.data.repository;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.ogif.kotae.utils.DateUtils;

import java.util.Date;

public class VoteCounterRepository {
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;

    public VoteCounterRepository() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Global.COLLECTION_USER);
    }

    private Task<Void> increment(@NonNull Record record, @Global.Collection String recordType, boolean isUpvote) {
        WriteBatch batch = db.batch();
        DocumentReference recordRef = db.collection(recordType).document(record.getId());
        batch.update(recordRef, isUpvote ?
                Record.Field.UPVOTE :
                Record.Field.DOWNVOTE, FieldValue.increment(1));
        return batch.commit();
    }

    private Task<Void> incrementWithAuthorXp(@NonNull Record record, @Global.Collection String recordType, boolean isUpvote) {
        return db.runTransaction(transaction -> {
            DocumentReference userRef = usersRef.document(record.getAuthorId());
            DocumentReference recordRef = db.collection(recordType).document(record.getId());
            DocumentSnapshot snapshot = transaction.get(userRef);
            transaction.update(recordRef, isUpvote
                    ? Record.Field.UPVOTE
                    : Record.Field.DOWNVOTE, FieldValue.increment(1));

            long changeInXp = isUpvote ? 1 : -1;
            Long xpDaily = snapshot.getLong(User.Field.XP_DAILY);
            Date startOfDay = DateUtils.startOfDay();
            Date xpDailyLastUpdate = snapshot.getDate(User.Field.XP_DAILY_LAST_UPDATE);

            if (xpDaily == null || xpDailyLastUpdate == null || xpDailyLastUpdate.before(startOfDay))
                xpDaily = changeInXp;
            else xpDaily += changeInXp;

            transaction.update(userRef, User.Field.XP, FieldValue.increment(changeInXp),
                    User.Field.XP_DAILY, xpDaily,
                    User.Field.XP_DAILY_LAST_UPDATE, new Date());
            return null;
        });
    }

    public Task<Void> increment(@NonNull Comment c, boolean isUpvote) {
        return increment(c, Global.COLLECTION_COMMENT, isUpvote);
    }

    public Task<Void> increment(@NonNull Post p, boolean isUpvote) {
        return p.getClass() == Question.class
                ? increment((Question) p, isUpvote)
                : increment((Answer) p, isUpvote);
    }

    public Task<Void> increment(@NonNull Question q, boolean isUpvote) {
        return incrementWithAuthorXp(q, Global.COLLECTION_QUESTION, isUpvote);
    }

    public Task<Void> increment(@NonNull Answer a, boolean isUpvote) {
        return incrementWithAuthorXp(a, Global.COLLECTION_ANSWER, isUpvote);
    }

    private Task<Void> decrement(@NonNull Record record, @Global.Collection String recordType, boolean isUpvote) {
        WriteBatch batch = db.batch();
        DocumentReference recordRef = db.collection(recordType).document(record.getId());
        batch.update(recordRef, isUpvote ?
                Record.Field.UPVOTE :
                Record.Field.DOWNVOTE, FieldValue.increment(-1));
        return batch.commit();
    }

    private Task<Void> decrementWithAuthorXp(@NonNull Record record, @Global.Collection String recordType, boolean isUpvote) {
        return db.runTransaction(transaction -> {
            DocumentReference userRef = usersRef.document(record.getAuthorId());
            DocumentReference recordRef = db.collection(recordType).document(record.getId());
            DocumentSnapshot snapshot = transaction.get(userRef);
            transaction.update(recordRef, isUpvote
                    ? Record.Field.UPVOTE
                    : Record.Field.DOWNVOTE, FieldValue.increment(-1));

            long changeInXp = isUpvote ? -1 : 1;
            Long xpDaily = snapshot.getLong(User.Field.XP_DAILY);
            Date startOfDay = DateUtils.startOfDay();
            Date xpDailyLastUpdate = snapshot.getDate(User.Field.XP_DAILY_LAST_UPDATE);

            if (xpDaily == null || xpDailyLastUpdate == null || xpDailyLastUpdate.before(startOfDay))
                xpDaily = changeInXp;
            else xpDaily += changeInXp;

            transaction.update(userRef, User.Field.XP, FieldValue.increment(changeInXp),
                    User.Field.XP_DAILY, xpDaily,
                    User.Field.XP_DAILY_LAST_UPDATE, new Date());
            return null;
        });
    }

    public Task<Void> decrement(@NonNull Comment c, boolean isUpvote) {
        return decrement(c, Global.COLLECTION_COMMENT, isUpvote);
    }

    public Task<Void> decrement(@NonNull Post p, boolean isUpvote) {
        return p.getClass() == Question.class
                ? decrement((Question) p, isUpvote)
                : decrement((Answer) p, isUpvote);
    }

    public Task<Void> decrement(@NonNull Question q, boolean isUpvote) {
        return decrementWithAuthorXp(q, Global.COLLECTION_QUESTION, isUpvote);
    }

    public Task<Void> decrement(@NonNull Answer a, boolean isUpvote) {
        return decrementWithAuthorXp(a, Global.COLLECTION_ANSWER, isUpvote);
    }

    public Task<Void> shift(@NonNull Record record, @Global.Collection String recordType, boolean fromUpvoteToDownvote) {
        WriteBatch batch = db.batch();
        DocumentReference recordRef = db.collection(recordType).document(record.getId());
        int changeInUpvote = fromUpvoteToDownvote ? -1 : 1;
        int changeInDownvote = fromUpvoteToDownvote ? 1 : -1;
        batch.update(recordRef, Record.Field.UPVOTE, FieldValue.increment(changeInUpvote),
                Record.Field.DOWNVOTE, FieldValue.increment(changeInDownvote));
        return batch.commit();
    }

    public Task<Void> shift(@NonNull Comment comment, boolean fromUpvoteToDownvote) {
        return shift(comment, Global.COLLECTION_COMMENT, fromUpvoteToDownvote);
    }
}

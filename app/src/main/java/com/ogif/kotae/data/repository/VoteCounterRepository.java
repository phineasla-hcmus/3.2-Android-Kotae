package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Record;
import com.ogif.kotae.data.model.User;

public class VoteCounterRepository {
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;

    public VoteCounterRepository() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Global.COLLECTION_USER);
    }

    public Task<Void> increment(@NonNull Record record, @Global.Collection String recordType, boolean isUpvote, boolean affectAuthorXp) {
        WriteBatch batch = db.batch();
        DocumentReference recordRef = db.collection(recordType).document(record.getId());
        batch.update(recordRef, isUpvote ?
                Record.Field.UPVOTE :
                Record.Field.DOWNVOTE, FieldValue.increment(1));
        if (affectAuthorXp) {
            DocumentReference userRef = usersRef.document(record.getAuthorId());
            int changeInXp = isUpvote ? 1 : -1;
            batch.update(userRef, User.Field.XP, FieldValue.increment(changeInXp));
        }
        return batch.commit();
    }

    public Task<Void> increment(@NonNull Comment comment, boolean isUpvote) {
        return increment(comment, Global.COLLECTION_COMMENT, isUpvote, false);
    }

    public Task<Void> increment(@NonNull Question q, boolean isUpvote) {
        return increment(q, Global.COLLECTION_QUESTION, isUpvote, true);
    }

    public Task<Void> increment(@NonNull Answer a, boolean isUpvote) {
        return increment(a, Global.COLLECTION_ANSWER, isUpvote, true);
    }

    public Task<Void> decrement(@NonNull Record record, @Global.Collection String recordType, boolean isUpvote, boolean affectAuthorXp) {
        WriteBatch batch = db.batch();
        DocumentReference recordRef = db.collection(recordType).document(record.getId());
        batch.update(recordRef, isUpvote ?
                Record.Field.UPVOTE :
                Record.Field.DOWNVOTE, FieldValue.increment(-1));
        if (affectAuthorXp) {
            DocumentReference userRef = usersRef.document(record.getAuthorId());
            int changeInXp = isUpvote ? -1 : 1;
            batch.update(userRef, User.Field.XP, FieldValue.increment(changeInXp));
        }
        return batch.commit();
    }

    public Task<Void> decrement(@NonNull Comment comment, boolean isUpvote) {
        return decrement(comment, Global.COLLECTION_COMMENT, isUpvote, false);
    }

    public Task<Void> decrement(@NonNull Question q, boolean isUpvote) {
        return decrement(q, Global.COLLECTION_QUESTION, isUpvote, true);
    }

    public Task<Void> decrement(@NonNull Answer a, boolean isUpvote) {
        return decrement(a, Global.COLLECTION_ANSWER, isUpvote, true);
    }

    public Task<Void> shift(@NonNull Record record, @Global.Collection String recordType, boolean fromUpvoteToDownvote, boolean affectAuthorXp) {
        WriteBatch batch = db.batch();
        DocumentReference recordRef = db.collection(recordType).document(record.getId());
        int changeInUpvote = fromUpvoteToDownvote ? -1 : 1;
        int changeInDownvote = fromUpvoteToDownvote ? 1 : -1;
        batch.update(recordRef, Record.Field.UPVOTE, FieldValue.increment(changeInUpvote),
                Record.Field.DOWNVOTE, FieldValue.increment(changeInDownvote));
        if (affectAuthorXp) {
            DocumentReference userRef = usersRef.document(record.getAuthorId());
            int changeInXp = fromUpvoteToDownvote ? -2 : 2;
            batch.update(userRef, User.Field.XP, FieldValue.increment(changeInXp));
        }
        return batch.commit();
    }

    public Task<Void> shift(@NonNull Comment comment, boolean fromUpvoteToDownvote) {
        return shift(comment, Global.COLLECTION_COMMENT, fromUpvoteToDownvote, false);
    }

    public Task<Void> shift(@NonNull Question q, boolean fromUpvoteToDownvote) {
        return shift(q, Global.COLLECTION_QUESTION, fromUpvoteToDownvote, true);
    }

    public Task<Void> shift(@NonNull Answer a, boolean fromUpvoteToDownvote) {
        return shift(a, Global.COLLECTION_ANSWER, fromUpvoteToDownvote, true);
    }
}

package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Record;

public class VoteCounterRepository {
    private final FirebaseFirestore db;
    private final CollectionReference votesRef;
    private final CollectionReference usersRef;

    public VoteCounterRepository() {
        db = FirebaseFirestore.getInstance();
        votesRef = db.collection(Global.COLLECTION_VOTE);
        usersRef = db.collection(Global.COLLECTION_USER);
    }

    // public Task<Void> incrementUpvote(@NonNull Record record, @Global.Collection String recordType, boolean affectUser) {
    //     CollectionReference recordsRef = db.collection(recordType);
    //     // recordsRef.document(record.getId()).update();
    // }
    //
    // public Task<Void> increment(@NonNull Record record, @Global.Collection String recordType) {
    //     return increment(record, recordType, true);
    // }
}

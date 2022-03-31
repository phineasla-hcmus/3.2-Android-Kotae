package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;

public class UserRepository {
    private static final String TAG = "UserRepository";
    protected final FirebaseFirestore db;
    protected final CollectionReference usersRef;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
    }

    public void getCurrentUser(@NonNull TaskListener.State<User> callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
            callback.onFailure(new NullPointerException("getCurrentUser returns null"));
        getById(auth.getCurrentUser().getUid(), callback);
    }

    public void getById(@NonNull String id, @NonNull TaskListener.State<User> callback) {
        usersRef.document(id).get().addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(User.class));
        }).addOnFailureListener(callback::onFailure);
    }
}

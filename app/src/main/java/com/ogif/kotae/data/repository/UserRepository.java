package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;

import java.util.Objects;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;

    public UserRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.usersRef = db.collection("users");
    }

    public void getCurrentUser(@NonNull TaskListener.State<User> callback) {
        if (auth.getCurrentUser() == null)
            callback.onFailure(new NullPointerException("getCurrentUser returns null"));
        getById(auth.getCurrentUser().getUid(), callback);
    }

    public void getById(@NonNull String id, @NonNull TaskListener.State<User> callback) {
        usersRef.document(id).get().addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(User.class));
        }).addOnFailureListener(callback::onFailure);
    }

    // public void login(@NonNull String email, @NonNull String password) {
    //     auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
    //         if (task.isSuccessful()) {
    //             FirebaseUser user = auth.getCurrentUser();
    //             mutableLiveData.postValue(StateWrapper.success(user));
    //         } else {
    //             String message = Objects.requireNonNull(task.getException()).getMessage();
    //             Log.w(TAG, message);
    //             mutableLiveData.postValue(StateWrapper.fail(message));
    //         }
    //     });
    // }

    public void login(@NonNull String email, @NonNull String password, TaskListener.State<FirebaseUser> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    if (callback != null)
                        callback.onSuccess(authResult.getUser());
                }).addOnFailureListener(callback::onFailure);
    }

    /**
     * @implNote <b>Usernames are not unique</b>. Firebase doesn't have unique index like MongoDB,
     * and Free Tier doesn't have Cloud Function, there are some workarounds:
     * <ol>
     * <li>
     *     Create a "usernames" collection, in which the key-value pair will be "username": "uid".
     *     See <a href="https://stackoverflow.com/a/59892127/12405558">Firestore unique index</a>.
     * </li>
     * <li>
     *     Validate on client-side. But since collection queries (check if username equal on Firestore)
     *     don't support atomic transaction, the result will be inconsistent (username might have been
     *     taken right after checking but before writing).
     * </li>
     * </ol>
     */
    public void createUser(@NonNull String email, @NonNull String password, @NonNull User extraInfo, TaskListener.State<FirebaseUser> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    FirebaseUser user = authResult.getUser();
                    if (user == null) {
                        Log.w(TAG, "createUserWithEmailAndPassword returns null");
                        return;
                    }
                    // Insert extraInfo into Firestore
                    usersRef.document(user.getUid()).set(extraInfo).addOnSuccessListener(aVoid -> {
                        if (callback != null)
                            callback.onSuccess(user);
                    }).addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void reload(@NonNull FirebaseUser user) {
        user.reload().addOnCompleteListener(task -> {
            // TODO
        });
    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    /**
     * @return true if the user's email is verified.
     * @throws NullPointerException if {@link FirebaseAuth#getCurrentUser()} null
     */
    public boolean isEmailVerified() {
        return Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified();
    }
}

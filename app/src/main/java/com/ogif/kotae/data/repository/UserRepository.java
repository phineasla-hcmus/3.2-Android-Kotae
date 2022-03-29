package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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
    private final MutableLiveData<StateWrapper<FirebaseUser>> mutableLiveData;

    public UserRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.mutableLiveData = new MutableLiveData<>();
        this.db = FirebaseFirestore.getInstance();
        this.usersRef = db.collection("users");
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            mutableLiveData.postValue(StateWrapper.success(user));
        }
    }

    public void login(@NonNull String email, @NonNull String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                mutableLiveData.postValue(StateWrapper.success(user));
            } else {
                String message = Objects.requireNonNull(task.getException()).getMessage();
                Log.w(TAG, message);
                mutableLiveData.postValue(StateWrapper.fail(message));
            }
        });
    }

    public void getCurrentUser(TaskListener.State<User> callback) {
        if (auth.getCurrentUser() == null)
            callback.onFailure(new NullPointerException("User haven't logged in"));
        getById(auth.getCurrentUser().getUid(), callback);
    }

    public void getById(@NonNull String id, TaskListener.State<User> callback) {
        usersRef.document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (callback != null)
                callback.onSuccess(documentSnapshot.toObject(User.class));
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
    public void createUser(@NonNull String email, @NonNull String password, @NonNull User extraInfo) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                FirebaseUser user = auth.getCurrentUser();
                // Insert extraInfo into Firestore
                usersRef.document(user.getUid()).set(extraInfo).addOnCompleteListener(extraTask -> {
                    if (task.isSuccessful())
                        mutableLiveData.postValue(StateWrapper.success(user));
                    else {
                        String message = Objects.requireNonNull(extraTask.getException())
                                .getMessage();
                        Log.w(TAG, message);
                        // Rollback user authentication
                        user.delete();
                        mutableLiveData.postValue(StateWrapper.fail(message));
                    }
                });
            } else {
                String message = Objects.requireNonNull(task.getException()).getMessage();
                Log.w(TAG, message);
                mutableLiveData.postValue(StateWrapper.fail(message));
            }
        });
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

    public MutableLiveData<StateWrapper<FirebaseUser>> getMutableLiveData() {
        return mutableLiveData;
    }
}

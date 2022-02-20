package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.data.model.User;

import java.util.Objects;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.userMutableLiveData = new MutableLiveData<>();

        if (auth.getCurrentUser() != null) {
            userMutableLiveData.postValue(auth.getCurrentUser());
        }
    }

    public void login(@NonNull String email, @NonNull String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                // boolean isNewUser = Objects.requireNonNull(task.getResult())
                //         .getAdditionalUserInfo().isNewUser();
                userMutableLiveData.postValue(user);
            } else {
                Log.w(TAG, Objects.requireNonNull(task.getException()).getMessage());
                userMutableLiveData.postValue(null);
            }
        });
    }

    public void createUser(@NonNull String email, @NonNull String password, @NonNull String username, @NonNull User extraInfo) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                FirebaseUser user = auth.getCurrentUser();

                Task<Void> taskUsername = user.updateProfile(
                        new UserProfileChangeRequest.Builder().setDisplayName(username).build());
                Task<Void> taskExtraInfo = db.collection("users")
                        .document(user.getUid())
                        .set(extraInfo);

                Tasks.whenAll(new Task[]{taskUsername, taskExtraInfo})
                        .addOnCompleteListener(extraTask -> {
                            if (task.isSuccessful())
                                userMutableLiveData.postValue(user);
                            else {
                                Log.w(TAG, Objects.requireNonNull(task.getException())
                                        .getMessage());
                                // Rollback user authentication
                                user.delete();
                                userMutableLiveData.postValue(null);
                            }
                        });
            } else {
                Log.w(TAG, Objects.requireNonNull(task.getException()).getMessage());
                userMutableLiveData.postValue(null);
            }
        });
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}

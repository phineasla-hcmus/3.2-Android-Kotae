package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;

import java.util.Objects;

public class AuthRepository extends UserRepository {
    private static final String TAG = "AuthRepository";
    private final FirebaseAuth auth;

    public AuthRepository() {
        super();
        this.auth = FirebaseAuth.getInstance();
    }

    @Nullable
    public FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }

    public void login(@NonNull String email, @NonNull String password, @Nullable TaskListener.State<FirebaseUser> callback) {
        Task<AuthResult> task = auth.signInWithEmailAndPassword(email, password);
        if (callback != null)
            task.addOnSuccessListener((AuthResult authResult) -> callback.onSuccess(authResult.getUser()))
                    .addOnFailureListener(callback::onFailure);
    }

    public void logout() {
        auth.signOut();
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
    public void createUser(@NonNull String email, @NonNull String password, @NonNull User extraInfo, @Nullable TaskListener.State<FirebaseUser> callback) {
        Task<AuthResult> authTask = auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> {
                    FirebaseUser user = authResult.getUser();
                    if (user == null) {
                        Log.w(TAG, "createUserWithEmailAndPassword returns null");
                        return;
                    }
                    // Insert extraInfo into Firestore
                    Task<Void> userTask = usersRef.document(user.getUid())
                            .set(extraInfo)
                            .addOnSuccessListener(aVoid -> {
                                if (callback != null)
                                    callback.onSuccess(user);
                            });
                    if (callback != null)
                        userTask.addOnFailureListener(callback::onFailure);
                });
        if (callback != null)
            authTask.addOnFailureListener(callback::onFailure);
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

    public void reAuthenticate(String password, TaskListener.State<Boolean> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), password);
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onSuccess(false);
            }
        });
    }

    public void updatePassword(String password, TaskListener.State<Boolean> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}

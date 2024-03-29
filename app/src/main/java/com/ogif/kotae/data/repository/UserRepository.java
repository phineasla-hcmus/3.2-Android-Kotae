package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.utils.model.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private static final String TAG = "UserRepository";
    protected final FirebaseFirestore db;
    protected final CollectionReference usersRef;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
    }

    public Task<User> getById(@NonNull String id) {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        usersRef.document(id).get().addOnSuccessListener(documentSnapshot ->
                taskCompletionSource.setResult(documentSnapshot.toObject(User.class))
        ).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();
    }

    public Task<User> getCurrentUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
            taskCompletionSource.setException(new NullPointerException("getCurrentUser returns null"));
            return taskCompletionSource.getTask();
        }
        return getById(auth.getCurrentUser().getUid());
    }

    private void onQueryListComplete(@NonNull Task<QuerySnapshot> query, @NonNull TaskListener.State<List<User>> callback) {
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> users = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                users.add(document.toObject(User.class));
            }
            callback.onSuccess(users);
        }).addOnFailureListener(callback::onFailure);
    }

    public void getById(@NonNull String id, @NonNull TaskListener.State<User> callback) {
        usersRef.document(id).get().addOnSuccessListener(documentSnapshot -> {
            callback.onSuccess(documentSnapshot.toObject(User.class));
        }).addOnFailureListener(callback::onFailure);
    }

    public void getCurrentUser(@NonNull TaskListener.State<User> callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
            callback.onFailure(new NullPointerException("getCurrentUser returns null"));
        getById(auth.getCurrentUser().getUid(), callback);
    }

    public void getLeaderboard(@NonNull String category, int limit, @NonNull TaskListener.State<List<User>> callback) {
        Task<QuerySnapshot> query = usersRef.whereEqualTo(User.Field.BLOCKED, false)
                .orderBy(category.equals("day")
                        ? User.Field.XP_DAILY
                        : User.Field.XP, Query.Direction.DESCENDING)
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getUserOrderByReport(@NonNull TaskListener.State<ArrayList<User>> callback) {
        ArrayList<User> userArrayList = new ArrayList<>();

        Query queryUser = usersRef.orderBy(User.Field.REPORT, Query.Direction.DESCENDING);
        queryUser.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                User user = documentSnapshot.toObject(User.class);
                user.setId(documentSnapshot.getId());
                userArrayList.add(user);
            }
            callback.onSuccess(userArrayList);
        }).addOnFailureListener(callback::onFailure);
    }

    public void blockUser(String userId, boolean blocked, TaskListener.State<Void> callback) {
        DocumentReference ref = usersRef.document(userId);
        ref.update("blocked", blocked).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onSuccess(unused);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void updateUser(String username, String job, int age, TaskListener.State<Boolean> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);
        updates.put("job", job);
        updates.put("yob", UserUtils.getYearOfBirth(age));
        usersRef.document(user.getUid())
                .update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onSuccess(true);
                    }
                });
    }
}

package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String TAG = "UserRepository";
    protected final FirebaseFirestore db;
    protected final CollectionReference usersRef;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
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

    public void getLeaderboard(@NonNull String category, int limit, @NonNull TaskListener.State<List<User>> callback) {
        Task<QuerySnapshot> query = usersRef.whereEqualTo(User.Field.BLOCKED, true)
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
}

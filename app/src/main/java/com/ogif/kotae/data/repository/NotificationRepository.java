package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Device;
import com.ogif.kotae.data.model.Notification;
import com.ogif.kotae.data.model.Record;

import java.util.ArrayList;

public class NotificationRepository {
    private static final String TAG = NotificationRepository.class.getName();
    private final FirebaseFirestore db;
    private final CollectionReference notificationsRef;
    private String userId = "";

    public NotificationRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.notificationsRef = db.collection("notification");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
    }

    public void getNotifications(@NonNull TaskListener.State<ArrayList<Notification>> callback) {
        Query queryNotification = notificationsRef.whereEqualTo("authorId", userId)
                .orderBy("time", Query.Direction.DESCENDING);
        queryNotification.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Notification> notifications = new ArrayList<Notification>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Notification notification = documentSnapshot.toObject(Notification.class);
                    notifications.add(notification);
                }
                callback.onSuccess(notifications);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void addNotification(Record record, String action) {
        action = formatStringAction(action);

        // For Testing
        String questionId = "FoiXrpHcy8AnRajVsen8";
        String authorId = "7it2MYXNJPSYBjbtRVAbRj7xE9i1";

        Notification notification = new Notification(questionId, authorId, userId, action);
        notificationsRef.add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: record added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void deleteNotification(Record record, String action) {
        action = formatStringAction(action);

        // For Testing
        String questionId = "FoiXrpHcy8AnRajVsen8";

        Query query = notificationsRef.whereEqualTo("questionId", questionId)
                .whereEqualTo("userId", userId)
                .whereEqualTo("userAction", action);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String notificationId = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    notificationId = documentSnapshot.getId();
                }
                if (!notificationId.equals("")) {
                    Log.d(TAG, notificationId);
                    // Delete device
                    notificationsRef.document(notificationId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Notification deleted");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Delete Failed: " + e.toString());
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

    }

    private String formatStringAction(String action) {
        String result = "";
        if (action == null) return result;
        if (action.toUpperCase().contains("UP")) {
            result = "UPVOTE";
        } else if (action.toUpperCase().contains("DO")) {
            result = "DOWNVOTE";
        } else {
            result = "COMMENT";
        }
        return result;
    }
}

package com.ogif.kotae.fcm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;

import java.util.Date;

public class Notification {
    private static final String TAG = Notification.class.getName();
    public static final String CHANNEL_UPVOTE_DOWNVOTE_ID = "UPVOTE_DOWNVOTE";
    public static final String CHANNEL_COMMENT_ID = "COMMENT";

    public Notification() {
    }

    public int getNotificationId() {
        return (int) new Date().getTime();
    }


    public void getToken(@NonNull TaskListener.State<String> callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            callback.onFailure(task.getException());
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        callback.onSuccess(token);
                    }
                });
    }


    public void pushUpvote(Question question, String userID) {

    }

    public void pushDownvoteNotification(Question question, String userID) {

    }

    public void pushCommentNotification(Question question, String userID) {

    }
}

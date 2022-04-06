package com.ogif.kotae.fcm;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> strMap = remoteMessage.getData();

        // 3 Cases: upvote, downvote, comment

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}

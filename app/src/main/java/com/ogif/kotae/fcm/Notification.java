package com.ogif.kotae.fcm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.ogif.kotae.R;

import java.util.Date;

public class Notification extends Activity {
    public static final String CHANNEL_UPVOTE_DOWNVOTE_ID = "UPVOTE_DOWNVOTE";
    public static final String CHANNEL_COMMENT_ID = "COMMENT";

    public Notification() {
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create Channel Upvote/Downvote
            CharSequence channelUpvoteDownvoteName = getString(R.string.channel_upvote_downvote_name);
            String channelUpvoteDownvoteDescription = getString(R.string.channel_upvote_downvote_description);
            NotificationChannel channelUpvoteDownvote = new NotificationChannel(CHANNEL_UPVOTE_DOWNVOTE_ID, channelUpvoteDownvoteName, NotificationManager.IMPORTANCE_DEFAULT);
            channelUpvoteDownvote.setDescription(channelUpvoteDownvoteDescription);

            // Create Channel Comment
            CharSequence channelCommentName = getString(R.string.channel_comment_name);
            String channelCommentDescription = getString(R.string.channel_comment_description);
            NotificationChannel channelComment = new NotificationChannel(CHANNEL_COMMENT_ID, channelCommentName, NotificationManager.IMPORTANCE_HIGH);
            channelComment.setDescription(channelCommentDescription);


            // Register two channels with the system;
            // You can't change the importance or other notification behaviors after this
            // If you want to change => Clear data of this app or uninstall and install again.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelUpvoteDownvote);
            notificationManager.createNotificationChannel(channelComment);
        }
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }
}

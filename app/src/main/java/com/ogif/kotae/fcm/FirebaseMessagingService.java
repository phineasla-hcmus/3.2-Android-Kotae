package com.ogif.kotae.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;

import java.util.Date;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String CHANNEL_UPVOTE_DOWNVOTE_ID = "UPVOTE_DOWNVOTE";
    public static final String CHANNEL_COMMENT_ID = "COMMENT";
    private static final String TAG = FirebaseMessagingService.class.getName();
    private Context context;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = this;
        createNotificationChannel();
        Map<String, String> strMap = remoteMessage.getData();

        String questionId = strMap.get("question_id");
        String username = strMap.get("username");
        String clarification = strMap.get("action");

        Log.d(TAG, questionId);
        Log.d(TAG, username);
        Log.d(TAG, clarification);

        switch (clarification) {
            case "UPVOTE": {
                sendNotificationByUpvote(questionId, username);
                break;
            }
            case "DOWNVOTE": {
                sendNotificationByDownvote(questionId, username);
                break;
            }
            case "COMMENT": {
                sendNotificationByComment(questionId, username);
                break;
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void sendNotificationByUpvote(String questionID, String usernameUpvote) {
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.getQuestionById(questionID, new TaskListener.State<Question>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onSuccess(Question result) {
                int notificationId = getNotificationId();
                String content = usernameUpvote + " just upvote your question.";

                Intent resultIntent = QuestionDetailActivity.newInstance(context, result);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

                stackBuilder.addNextIntentWithParentStack(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(notificationId,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_UPVOTE_DOWNVOTE_ID)
                        .setSmallIcon(R.drawable.ic_upvote)
                        .setLargeIcon(bitmap)
                        .setContentTitle("Question upvote")
                        .setContentText(content)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationId, builder.build());
            }
        });
    }

    private void sendNotificationByDownvote(String questionID, String usernameDownvote) {
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.getQuestionById(questionID, new TaskListener.State<Question>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onSuccess(Question result) {
                int notificationId = getNotificationId();
                String content = usernameDownvote + " just downvote your question.";

                Intent resultIntent = QuestionDetailActivity.newInstance(context, result);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

                stackBuilder.addNextIntentWithParentStack(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(notificationId,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_UPVOTE_DOWNVOTE_ID)
                        .setSmallIcon(R.drawable.ic_downvote)
                        .setLargeIcon(bitmap)
                        .setContentTitle("Question downvote")
                        .setContentText(content)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationId, builder.build());
            }
        });
    }

    private void sendNotificationByComment(String questionID, String usernameComment) {
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.getQuestionById(questionID, new TaskListener.State<Question>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onSuccess(Question result) {
                int notificationId = getNotificationId();
                String content = usernameComment + " just commented your question.";

                Intent resultIntent = QuestionDetailActivity.newInstance(context, result);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

                stackBuilder.addNextIntentWithParentStack(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(notificationId,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_UPVOTE_DOWNVOTE_ID)
                        .setSmallIcon(R.drawable.ic_baseline_comment)
                        .setLargeIcon(bitmap)
                        .setContentTitle("Question comment")
                        .setContentText(content)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationId, builder.build());
            }
        });
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    public void createNotificationChannel() {
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
}

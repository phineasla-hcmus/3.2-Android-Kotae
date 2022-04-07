package com.ogif.kotae.fcm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Device;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.DeviceRepository;
import com.ogif.kotae.data.repository.UserRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification {
    private static final String TAG = Notification.class.getName();
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAAedZ3mgM:APA91bEs7a8Pdpj3hWVDMk4cXsvGuhPc6qDcE6ALpXAQvWJ0YR8-R-KPLO94zZsn_4mp-rwGO1-jy2Kaw7OAYQ_qvIm-yqNUx9CV6ETAue6xNB2eTFyG9aGMFygbAzcEBeotq95V1gfJ";

    public Notification() {

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

    private void pushNotification(Context context, Question question, String action) {
        DeviceRepository deviceRepository = new DeviceRepository();
        deviceRepository.getDevices(question.getAuthorId(), new TaskListener.State<ArrayList<Device>>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onSuccess(ArrayList<Device> devices) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                RequestQueue queue = Volley.newRequestQueue(context);

                UserRepository userRepository = new UserRepository();
                userRepository.getById(FirebaseAuth.getInstance().getCurrentUser().getUid(), new TaskListener.State<User>() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.toString());
                    }

                    @Override
                    public void onSuccess(User userPushNotification) {
                        for (Device device : devices) {
                            try {
                                JSONObject data = new JSONObject();
                                data.put("questionId", question.getId());
                                data.put("username", userPushNotification.getUsername());
                                data.put("action", action);

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("to", device.getToken());
                                jsonObject.put("data", data);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                        Request.Method.POST,
                                        BASE_URL,
                                        jsonObject,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d(TAG, "onResponse: " + response.toString());
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e(TAG, "onErrorResponse: " + error.toString());
                                            }
                                        }) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("Content-Type", "application/json");
                                        params.put("Authorization", SERVER_KEY);
                                        return params;
                                    }
                                };

                                queue.add(jsonObjectRequest);
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString() );
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    public void pushUpvote(Context context, Question question) {
        String action = "UPVOTE";
        pushNotification(context, question, action);
    }

    public void pushDownvoteNotification(Context context, Question question) {
        String action = "DOWNVOTE";
        pushNotification(context, question, action);
    }

    public void pushCommentNotification(Context context, Question question) {
        String action = "COMMENT";
        pushNotification(context, question, action);
    }
}

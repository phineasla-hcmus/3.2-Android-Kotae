package com.ogif.kotae.data.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.ui.createquestion.CreateQuestionActivity;
import com.ogif.kotae.ui.main.ImageAdapter;
import com.ogif.kotae.utils.model.UserUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.transform.Result;

public class StorageRepository {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference ref;
    private List<String> url = new ArrayList<>();

    public StorageRepository() {
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference();
        this.ref = storageRef.child("questions/");
    }

    public List<String> getUrl() {
        return url;
    }

    public Task<List<String>> uploadQuestionImages(ArrayList<Uri> imageList, int uploadCount, Context context, ImageAdapter imageAdapter, String name) {
        List<Task<UploadTask.TaskSnapshot>> tasks = new ArrayList<>();
        List<StorageReference> imageRefs = new ArrayList<>();
        TaskCompletionSource<List<String>> taskCompletionSource = new TaskCompletionSource<>();
        for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {
            Uri individualImage = imageList.get(uploadCount);
            String file = individualImage.getLastPathSegment() + name + ".jpg";

            StorageReference imageName = ref.child(file);
            tasks.add(imageName.putFile(individualImage));
            imageRefs.add(imageName);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                List<Task<Uri>> uriTasks = new ArrayList<>();
                for (int i = 0; i < objects.size(); i++) {
                    UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) objects.get(i);
                    uriTasks.add(imageRefs.get(i).getDownloadUrl());
                }
                Tasks.whenAllSuccess(uriTasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        List<String> uris = new ArrayList<>();
                        for (Object object : objects) {
                            Uri uri = (Uri) object;
                            uris.add(uri.toString());
                        }
                        taskCompletionSource.setResult(uris);
                    }
                }).addOnFailureListener(taskCompletionSource::setException);
            }
        }).addOnFailureListener(taskCompletionSource::setException);
        return taskCompletionSource.getTask();

    }

}

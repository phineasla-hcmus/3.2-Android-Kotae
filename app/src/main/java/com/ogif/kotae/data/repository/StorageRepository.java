package com.ogif.kotae.data.repository;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ogif.kotae.ui.createquestion.CreateQuestionActivity;
import com.ogif.kotae.ui.main.ImageAdapter;

import java.util.ArrayList;

import javax.xml.transform.Result;

public  class StorageRepository {
    private  FirebaseStorage storage ;
    private  StorageReference storageRef ;
    private StorageReference ref ;

    public StorageRepository(){
        this.storage = FirebaseStorage.getInstance();
        this. storageRef = storage.getReference();
        this.ref = storageRef.child("questions/");
    }
    public void uploadQuestionImages(ArrayList<Uri> imageList, int uploadCount, Context context,  ImageAdapter imageAdapter) {
        for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {
            Uri IndividualImage = imageList.get(uploadCount);
            StorageReference ImageName = ref.child(IndividualImage.getLastPathSegment());
            ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast
                            .makeText(context,
                                    "Uploaded!!",
                                    Toast.LENGTH_SHORT)
                            .show();
                    imageList.clear();
                    imageAdapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast
                            .makeText(context,
                                    "Failed to upload " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                    imageList.clear();
                    imageAdapter.notifyDataSetChanged();
                }
            });

        }
    }

}

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

public  class StorageRepository {
    private  FirebaseStorage storage ;
    private  StorageReference storageRef ;
    private StorageReference ref ;

    public StorageRepository(){
        this.storage = FirebaseStorage.getInstance();
        this. storageRef = storage.getReference();
        this.ref = storageRef.child("questions/");
    }
    public void uploadQuestionImages(ArrayList<Uri> imageList, int uploadCount, Context context,  ImageAdapter imageAdapter,String name, List<String> imgIds) {
        for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {
            Uri IndividualImage = imageList.get(uploadCount);
            String file = IndividualImage.getLastPathSegment() +name+".jpg";
            imgIds.add(file);
            StorageReference ImageName = ref.child(file);
            ImageName.putFile(IndividualImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Toast
                                .makeText(context,
                                        "Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                        imageList.clear();
                        imageAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast
                                .makeText(context,
                                        "Failed to upload ",
                                        Toast.LENGTH_SHORT)
                                .show();
                        imageList.clear();
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

}

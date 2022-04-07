package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeviceRepository {
    private static final String TAG = DeviceRepository.class.getName();
    private final FirebaseFirestore db;
    private final CollectionReference devicesRef;

    public DeviceRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.devicesRef = db.collection("devices");
    }

    public void getDevices(String userId, @NonNull TaskListener.State<ArrayList<Device>> callback) {
        Query queryDevice = devicesRef.whereEqualTo("userId", userId);
        queryDevice.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Device> devices = new ArrayList<Device>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Device device = documentSnapshot.toObject(Device.class);
                    devices.add(device);
                }
                callback.onSuccess(devices);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }


    public void addDevice(Device device) {
        // Add new if token isn't exist
        Query query = devicesRef.whereEqualTo("userId", device.getUserId())
                .whereEqualTo("token", device.getToken());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Device> devices = new ArrayList<Device>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Device deviceTemp = documentSnapshot.toObject(Device.class);
                    devices.add(deviceTemp);
                }
                if (devices.size() == 0) {
                    // Add device
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("userId", device.getUserId());
                    docData.put("token", device.getToken());
                    devicesRef.add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Device added: " + documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: " + e.toString());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AAA", "onFailure: " + e.toString());
            }
        });
    }

    public void removeDevice(Device device) {
        Query query = devicesRef.whereEqualTo("userId", device.getUserId())
                .whereEqualTo("token", device.getToken());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Device> devices = new ArrayList<Device>();
                String deviceId = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    deviceId = documentSnapshot.getId();
                }
                if (!deviceId.equals("")) {
                    // Delete device
                    devicesRef.document(deviceId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Device deleted");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Delete Failed: " + e.toString() );
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AAA", "onFailure: " + e.toString());
            }
        });
    }
}

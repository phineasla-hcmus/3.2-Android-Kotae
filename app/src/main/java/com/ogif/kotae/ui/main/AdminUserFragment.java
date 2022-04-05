package com.ogif.kotae.ui.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.data.repository.UserRepository;
import com.ogif.kotae.ui.admin.AdminUserAdapter;

import java.util.ArrayList;


public class AdminUserFragment extends Fragment {
    private ArrayList<User> userArrayList;
    private ListView lvUser;
    private AdminUserAdapter userAdapter;
    private UserRepository userRepository;


    public AdminUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);
        lvUser = (ListView) view.findViewById(R.id.lv_user_admin);

        userArrayList = new ArrayList<User>();

        userRepository = new UserRepository();
        userRepository.getUserOrderByReport(new TaskListener.State<ArrayList<User>>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AAA", e.toString());
            }

            @Override
            public void onSuccess(ArrayList<User> result) {
                userArrayList.addAll(result);
                userAdapter = new AdminUserAdapter(getActivity(), R.layout.item_user_admin, result);
                lvUser.setAdapter(userAdapter);
                lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        confirmAndHandleBlockOrUnblock(i);
                    }
                });
            }
        });
        return view;
    }

    private void confirmAndHandleBlockOrUnblock(int pos) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        // Handle Block
        if (!userArrayList.get(pos).isBlocked()) {
            alertBuilder.setTitle("Confirm Block User");
            alertBuilder.setMessage("Are you sure you want to block this user?");
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userArrayList.get(pos).setBlocked(true);
                    userAdapter.notifyDataSetChanged();

                    userRepository.blockUser(userArrayList.get(pos).getId(), true, new TaskListener.State<Void>() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AAA", e.toString());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            // TO DO
                        }
                    });
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertBuilder.show();
        }
        // Handle Unblock
        else {
            alertBuilder.setTitle("Confirm Unblock User");
            alertBuilder.setMessage("Are you sure you want to unblock this user?");
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userArrayList.get(pos).setBlocked(false);
                    userAdapter.notifyDataSetChanged();

                    userRepository.blockUser(userArrayList.get(pos).getId(), false, new TaskListener.State<Void>() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AAA", e.toString());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            // TO DO
                        }
                    });
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertBuilder.show();
        }
    }
}
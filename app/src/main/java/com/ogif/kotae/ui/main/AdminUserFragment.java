package com.ogif.kotae.ui.main;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.ui.admin.AdminUserAdapter;

import java.util.ArrayList;


public class AdminUserFragment extends Fragment {

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
        ListView lvUser = (ListView) view.findViewById(R.id.lv_user_admin);

        ArrayList<User> userArrayList = new ArrayList<User>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query queryUser = db.collection("users").orderBy("report", Query.Direction.DESCENDING);
        queryUser.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    user.setId(documentSnapshot.getId());
                    userArrayList.add(user);
                }

                AdminUserAdapter userAdapter = new AdminUserAdapter(getActivity(), R.layout.item_user_admin, userArrayList);
                lvUser.setAdapter(userAdapter);
                lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("AAA", "User: " + userArrayList.get(i).getId()
                                + ",username:" + userArrayList.get(i).getUsername()
                                + ",isBlocked:" + String.valueOf(userArrayList.get(i).isBlocked())
                                + ",report:" + String.valueOf(userArrayList.get(i).getReport())
                                + ",role:" + userArrayList.get(i).getRole()
                                + ",yob:" + userArrayList.get(i).getYob()
                                + ",avatar:" + userArrayList.get(i).getAvatar());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AAA", e.toString());
            }
        });

        return view;
    }
}
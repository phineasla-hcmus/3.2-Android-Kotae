package com.ogif.kotae.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.admin.AdminQuestionAdapter;

import java.util.ArrayList;

public class AdminQuestionFragment extends Fragment {
    private FirebaseFirestore db;
    private ArrayList<Question> questionArrayList;
    private ListView lvQuestion;
    public AdminQuestionAdapter questionAdapter;

    public AdminQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_question, container, false);

        lvQuestion = (ListView) view.findViewById(R.id.lv_question_admin);

        questionArrayList = new ArrayList<Question>();

        db = FirebaseFirestore.getInstance();
        Query queryQuestion = db.collection("questions").orderBy("report", Query.Direction.DESCENDING);
        queryQuestion.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Question question = documentSnapshot.toObject(Question.class);
                    questionArrayList.add(question);
                }
                questionAdapter = new AdminQuestionAdapter(getActivity(), R.layout.item_question, questionArrayList);
                lvQuestion.setAdapter(questionAdapter);
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
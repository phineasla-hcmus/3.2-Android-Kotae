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
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.ui.admin.AdminQuestionAdapter;

import java.util.ArrayList;

public class AdminQuestionFragment extends Fragment {
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

        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.getQuestionsOrderByReport(new TaskListener.State<ArrayList<Question>>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AAA", e.toString());
            }

            @Override
            public void onSuccess(ArrayList<Question> result) {
                questionAdapter = new AdminQuestionAdapter(getActivity(), R.layout.item_question, result);
                lvQuestion.setAdapter(questionAdapter);
            }
        });
        return view;
    }
}
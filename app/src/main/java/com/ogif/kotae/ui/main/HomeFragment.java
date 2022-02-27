package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.utils.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    //    private View homeView;
    private FloatingActionButton fabAddQuestion;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setUpRecyclerView();

        fabAddQuestion = (FloatingActionButton) binding.fabAddQuestion;

        fabAddQuestion.setOnClickListener(v -> {
            startCreateQuestionActivity();
        });

        return binding.getRoot();
    }

    private void startCreateQuestionActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CreateQuestionActivity.class);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) binding.rvHome;

        QuestionAdapter adapter = new QuestionAdapter(questionList(), this.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));
        recyclerView.setAdapter(adapter);
    }

    private List<Question> questionList() {
        List<Question> questionList = new ArrayList<>();
        String date = "22/01/2022";
        questionList.add(new Question
                ("Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        return questionList;
    }
}
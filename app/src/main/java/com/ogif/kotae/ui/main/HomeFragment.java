package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.utils.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    //    private View homeView;
    private FloatingActionButton fabAddQuestion;
    private FragmentHomeBinding binding;
    SwipeRefreshLayout swipeLayout;

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
        swipeLayout = (SwipeRefreshLayout) binding.swipeContainer;
        swipeLayout.setOnRefreshListener(this);
        return binding.getRoot();
    }

    @Override
    public void onRefresh() {
       // Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
        //TODO: do refresh task
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
                ("Title","Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Title","Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Title","Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Title","Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Title","Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));
        questionList.add(new Question
                ("Title","Chau", "Lớp 10", "Tiếng Anh",
                        "How old are you?", date));

        return questionList;
    }
}
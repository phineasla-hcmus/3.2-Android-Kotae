package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.fcm.Notification;
import com.ogif.kotae.ui.QuestionViewModel;
import com.ogif.kotae.ui.createquestion.CreateQuestionActivity;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static final String TAG = "Home";

    private ActivityResultLauncher<Intent> questionDetailResultLauncher;
    private FloatingActionButton fabAddQuestion;
    private FragmentHomeBinding binding;
    private SwipeRefreshLayout swipeLayout;
    private QuestionRepository questionRepository;
    private HomeAdapter adapter;
    private QuestionViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // homeView = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        questionRepository = new QuestionRepository();
        viewModel = new QuestionViewModel();

        setUpRecyclerView();
        fabAddQuestion = (FloatingActionButton) binding.fabAddQuestion;

        fabAddQuestion.setOnClickListener(v -> {
            startCreateQuestionActivity();
        });
        swipeLayout = (SwipeRefreshLayout) binding.swipeContainer;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                        .setQuery(questionRepository.getHomeQuestions(), Question.class)
                        .build();
                adapter = new HomeAdapter(options, requireActivity());
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });

        adapter.setOnVoteChangeListener((position, voteView, previous, current) -> {
            Post holder = (Post) voteView.getHolder();
            if (holder == null) {
                Log.w(TAG, "Unidentified holder for VoteView, did you forget to setHolder()?");
                return;
            }
            Notification notification = new Notification();
            notification.pushUpvoteNotification(getContext(), holder);
            viewModel.updateVote(holder, previous, current);
        });
        return binding.getRoot();
    }


    private void startCreateQuestionActivity() {
        Intent intent = new Intent(requireActivity().getApplicationContext(), CreateQuestionActivity.class);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) binding.rvHome;

        //adapter = new HomeAdapter(questionList(), this.getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 1));

        FirestoreRecyclerOptions<Question> options;

        if (getArguments() != null) {
            String sort = getArguments().getString("sort");
            String status = getArguments().getString("status");
            ArrayList<String> lstCourses = getArguments().getStringArrayList("lstCourses");
            ArrayList<String> lstGrades = getArguments().getStringArrayList("lstGrades");
            options = new FirestoreRecyclerOptions.Builder<Question>()
                    .setQuery(questionRepository.getFilterQuestionQuery(sort, status, lstGrades, lstCourses), Question.class)
                    .build();
        } else {
            options = new FirestoreRecyclerOptions.Builder<Question>()
                    .setQuery(questionRepository.getHomeQuestions(), Question.class)
                    .build();
        }
        adapter = new HomeAdapter(options, requireActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
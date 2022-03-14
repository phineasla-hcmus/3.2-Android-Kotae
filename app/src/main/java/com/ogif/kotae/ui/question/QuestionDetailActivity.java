package com.ogif.kotae.ui.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivityQuestionDetailBinding;

public class QuestionDetailActivity extends AppCompatActivity {
    public static final String BUNDLE_QUESTION = "question";
    private ActivityQuestionDetailBinding binding;
    private RecyclerView recyclerView;
    private QuestionDetailAdapter adapter;
    private QuestionDetailViewModel questionDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbarQuestionDetail);
        setTitle("");

        adapter = new QuestionDetailAdapter();
        recyclerView = binding.recyclerViewQuestionDetail;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(adapter);

        QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory((Question) new Question()
                .setTitle("this is a title")
                .setContent("GET HELP, HELPPPPPPPPPP!!!!")
                .setAuthorId("123").setSubjectId("s01"));
        // QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory(savedInstanceState
        //         .getParcelable(BUNDLE_QUESTION));
        questionDetailViewModel = new ViewModelProvider(this, factory)
                .get(QuestionDetailViewModel.class);
        questionDetailViewModel.getQuestionLiveData().observe(this, question -> {
            adapter.updateQuestion(question);
        });

    }

    public static Intent newInstance(Context context, @NonNull Question question) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(BUNDLE_QUESTION, question);
        return intent;
    }
}
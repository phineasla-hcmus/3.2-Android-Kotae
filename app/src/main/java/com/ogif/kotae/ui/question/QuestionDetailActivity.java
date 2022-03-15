package com.ogif.kotae.ui.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivityQuestionDetailBinding;

import java.util.Objects;

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

        MaterialToolbar toolbar = binding.toolbarQuestionDetail;
        toolbar.setNavigationOnClickListener(nav -> {
            NavUtils.navigateUpFromSameTask(this);
        });
        setSupportActionBar(toolbar);

        adapter = new QuestionDetailAdapter();
        recyclerView = binding.recyclerViewQuestionDetail;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(adapter);

        // QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory((Question) new Question()
        //         .setTitle("this is a title")
        //         .setContent("GET HELP, HELPPPPPPPPPP!!!!")
        //         .setAuthorId("123").setSubjectId("s01"));
        QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory(getIntent().getExtras()
                .getParcelable(BUNDLE_QUESTION));
        questionDetailViewModel = new ViewModelProvider(this, factory)
                .get(QuestionDetailViewModel.class);
        questionDetailViewModel.getQuestionLiveData().observe(this, question -> {
            adapter.updateQuestion(question);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question_detail, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(BUNDLE_QUESTION, questionDetailViewModel
                .getQuestionLiveData()
                .getValue());
    }

    public static Intent newInstance(Context context, @NonNull Question question) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(BUNDLE_QUESTION, question);
        return intent;
    }
}
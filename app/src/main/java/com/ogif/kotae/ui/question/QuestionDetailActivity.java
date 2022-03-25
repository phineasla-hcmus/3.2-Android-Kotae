package com.ogif.kotae.ui.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.ogif.kotae.ui.main.CreateAnswerActivity;
import com.ogif.kotae.ui.question.adapter.QuestionDetailAdapter;

public class QuestionDetailActivity extends AppCompatActivity {
    public static final String BUNDLE_QUESTION = "question";
    private ActivityQuestionDetailBinding binding;
    private QuestionDetailAdapter adapter;
    private QuestionDetailViewModel questionDetailViewModel;
    private RecyclerView recyclerView;

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

        adapter = new QuestionDetailAdapter(this);
        recyclerView = binding.recyclerViewQuestionDetail;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(adapter);

        Question questionFromExtra = getIntent().getExtras().getParcelable(BUNDLE_QUESTION);

        QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory(questionFromExtra);
        questionDetailViewModel = new ViewModelProvider(this, factory)
                .get(QuestionDetailViewModel.class);

        questionDetailViewModel.getAnswers();

        questionDetailViewModel.getQuestionLiveData().observe(this, question -> {
            adapter.updateQuestion(question);
        });
        questionDetailViewModel.getAnswerLiveData().observe(this, answers -> {
            adapter.updateAnswers(answers);
        });

        binding.btnQuestionAnswer.setOnClickListener(v -> {
            startCreateAnswerActivity();
        });
    }

    private void startCreateAnswerActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateAnswerActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question_detail, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
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
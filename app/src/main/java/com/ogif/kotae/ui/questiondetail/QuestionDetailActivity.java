package com.ogif.kotae.ui.questiondetail;

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
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.databinding.ActivityQuestionDetailBinding;
import com.ogif.kotae.fcm.Notification;
import com.ogif.kotae.ui.VoteView;
import com.ogif.kotae.ui.createanswer.CreateAnswerActivity;
import com.ogif.kotae.ui.questiondetail.adapter.QuestionDetailAdapter;

public class QuestionDetailActivity extends AppCompatActivity {
    public static final String TAG = "QuestionDetailActivity";
    public static final String BUNDLE_QUESTION = "question";

    private ActivityQuestionDetailBinding binding;
    private QuestionDetailAdapter adapter;
    private QuestionDetailViewModel questionDetailViewModel;
    private RecyclerView recyclerView;
    private String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MaterialToolbar toolbar = binding.toolbarQuestionDetail;
        toolbar.setNavigationOnClickListener(nav -> NavUtils.navigateUpFromSameTask(this));
        setSupportActionBar(toolbar);

        binding.btnQuestionAnswer.setOnClickListener(v -> startCreateAnswerActivity());

        adapter = new QuestionDetailAdapter(this);
        recyclerView = binding.recyclerViewQuestionDetail;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnVoteChangeListener((voteView, position, previous, current) -> {
            Post holder = (Post) voteView.getHolder();
            if (holder == null) {
                Log.w(TAG, "Unidentified holder for VoteView, did you forget to setHolder()?");
                return;
            }
            Notification notification = new Notification();
            notification.pushUpvoteNotification(getApplicationContext(), holder);
            questionDetailViewModel.updateVote(holder, position, previous, current);
        });

        Question questionFromExtra = getIntent().getExtras().getParcelable(BUNDLE_QUESTION);
        this.questionId = questionFromExtra.getId();

        QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory(questionFromExtra);
        questionDetailViewModel = new ViewModelProvider(this, factory)
                .get(QuestionDetailViewModel.class);

        // questionDetailViewModel.getQuestionLiveData().observe(this, question -> {
        //     if (question == null) {
        //         // TODO fetch question failed
        //         return;
        //     }
        //     adapter.setQuestion(question);
        // });
        questionDetailViewModel.getPostLiveData().observe(this, posts -> {
            adapter.setData(posts);
        });

        questionDetailViewModel.getAnswers();
    }

    private void startCreateAnswerActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateAnswerActivity.class);
        intent.putExtra("questionId", questionId);
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
        // savedInstanceState.putParcelable(BUNDLE_QUESTION, questionDetailViewModel
        //         .getQuestionLiveData()
        //         .getValue());
    }

    @NonNull
    public static Intent newInstance(@NonNull Context context, @NonNull Question question) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(BUNDLE_QUESTION, question);
        return intent;
    }
}
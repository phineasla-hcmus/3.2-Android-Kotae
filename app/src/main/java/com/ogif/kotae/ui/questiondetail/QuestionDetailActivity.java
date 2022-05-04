package com.ogif.kotae.ui.questiondetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivityQuestionDetailBinding;
import com.ogif.kotae.fcm.Notification;
import com.ogif.kotae.ui.createanswer.CreateAnswerActivity;
import com.ogif.kotae.ui.questiondetail.adapter.QuestionDetailAdapter;
import com.ogif.kotae.utils.ui.LazyLoadScrollListener;

import java.util.Objects;

public class QuestionDetailActivity extends AppCompatActivity {
    public static final String TAG = "QuestionDetailActivity";
    public static final String BUNDLE_QUESTION = "question";

    private ActivityQuestionDetailBinding binding;
    private QuestionDetailAdapter adapter;
    private QuestionDetailViewModel viewModel;
    private RecyclerView recyclerView;
    private String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MaterialToolbar toolbar = binding.toolbarQuestionDetail;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(nav -> {
            NavUtils.navigateUpFromSameTask(this);
            // Intent intent = new Intent();
            // intent.putExtra(BUNDLE_QUESTION, viewModel.getLocalQuestion());
            // setResult(RESULT_OK, intent);
            // finish();
        });

        binding.btnQuestionAnswer.setOnClickListener(v -> startCreateAnswerActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new QuestionDetailAdapter(this);
        recyclerView = binding.recyclerViewQuestionDetail;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new LazyLoadScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                viewModel.getAnswers();
            }
        });

        adapter.setOnVoteChangeListener((position, voteView, previous, current) -> {
            Post holder = (Post) voteView.getHolder();
            if (holder == null) {
                Log.w(TAG, "Unidentified holder for VoteView, did you forget to setHolder()?");
                return;
            }
            Notification notification = new Notification();
            notification.pushUpvoteNotification(getApplicationContext(), holder);
            viewModel.updateVote(holder, previous, current);
        });

        Question questionFromExtra = getIntent().getExtras().getParcelable(BUNDLE_QUESTION);
        this.questionId = questionFromExtra.getId();

        QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory(questionFromExtra);
        viewModel = new ViewModelProvider(this, factory)
                .get(QuestionDetailViewModel.class);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            refresh();
        });

        viewModel.getPostsLiveData().observe(this, latestPosts -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            adapter.setItems(viewModel.getImmutableLocalPosts());
            adapter.forceUpdateFooter();
        });
    }

    /**
     * Obtain an {@link Intent} that will launch an explicit target activity
     * specified by sourceActivity's {@link NavUtils#PARENT_ACTIVITY} &lt;meta-data&gt;
     * element in the application's manifest. If the device is running
     * Jellybean or newer, the android:parentActivityName attribute will be preferred
     * if it is present.
     *
     * @return a new Intent targeting the defined parent activity of sourceActivity
     */
    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        if (intent != null)
            intent.putExtra(BUNDLE_QUESTION, viewModel.getLocalQuestion());
        setResult(RESULT_OK, intent);
        return intent;
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
        savedInstanceState.putParcelable(BUNDLE_QUESTION, viewModel.getLocalQuestion());
    }

    public void refresh() {
        viewModel.getAll(Objects.requireNonNull(viewModel.getLocalQuestion()).getId());
    }

    @NonNull
    public static Intent newInstance(@NonNull Context context, @NonNull Question question) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(BUNDLE_QUESTION, question);
        return intent;
    }
}
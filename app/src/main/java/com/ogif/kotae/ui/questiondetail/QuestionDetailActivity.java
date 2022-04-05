package com.ogif.kotae.ui.questiondetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.databinding.ActivityQuestionDetailBinding;
import com.ogif.kotae.ui.CommentViewModel;
import com.ogif.kotae.ui.VoteView;
import com.ogif.kotae.ui.createanswer.CreateAnswerActivity;
import com.ogif.kotae.ui.questiondetail.adapter.CommentAdapter;
import com.ogif.kotae.ui.questiondetail.adapter.QuestionDetailAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionDetailActivity extends AppCompatActivity {
    public static final String BUNDLE_QUESTION = "question";

    private ActivityQuestionDetailBinding binding;
    private QuestionDetailAdapter adapter;
    private CommentAdapter commentAdapter;
    private QuestionDetailViewModel questionDetailViewModel;
    private static CommentViewModel commentViewModel;
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
        // recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(adapter);

        adapter.setOnVoteChangeListener(new VoteView.OnStateChangeListener() {
            @Override
            public void onUpvote(VoteView view, boolean isActive) {
                // TODO change vote state on firebase
            }

            @Override
            public void onDownvote(VoteView view, boolean isActive) {
                // TODO change vote state on firebase
            }
        });

        Question questionFromExtra = getIntent().getExtras().getParcelable(BUNDLE_QUESTION);
        questionId = questionFromExtra.getId();

        QuestionDetailViewModelFactory factory = new QuestionDetailViewModelFactory(questionFromExtra);
        questionDetailViewModel = new ViewModelProvider(this, factory)
                .get(QuestionDetailViewModel.class);

        fetchAndObserve();

        commentAdapter = new CommentAdapter(this);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        // FirebaseFirestore db = FirebaseFirestore.getInstance();
        // db.collection("votes")
        //         .whereEqualTo("authorId", "0FDZ97sbxRf17ac07Sx260inaPR2")
        //         .whereIn(FieldPath.documentId(), Arrays.asList("5dEcqTo35b9QA1XoGg2o",
        //                 "EZdvEFlTfBzv3QCcRhjI", "EZdvEFlTfBzv3QCcRhjI", "EZdvEFlTfBzv3QCcRhjI",
        //                 "EZdvEFlTfBzv3QCcRhjI", "EZdvEFlTfBzv3QCcRhjI", "EZdvEFlTfBzv3QCcRhjI",
        //                 "EZdvEFlTfBzv3QCcRhjI", "EZdvEFlTfBzv3QCcRhjI", "EZdvEFlTfBzv3QCcRhjI"))
        //         .get()
        //         .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        //             @Override
        //             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        //                 for (DocumentSnapshot doc : queryDocumentSnapshots) {
        //                     Vote v = doc.toObject(Vote.class);
        //                     Log.d("TEST", v.getId());
        //                 }
        //             }
        //         });
    }

    public void fetchAndObserve() {
        questionDetailViewModel.getAnswers();

        questionDetailViewModel.getQuestionLiveData().observe(this, question -> {
            if (question == null) {
                // TODO fetch question failed
                return;
            }
            adapter.updateQuestion(question);
        });
        questionDetailViewModel.getQuestionVoteLiveData().observe(this, vote -> {
            if (vote == null) {
                // Current vote for question is NONE or fetching failed, ignore
                return;
            }
            adapter.updateQuestionVote(vote);
        });
        questionDetailViewModel.getAnswerLiveData().observe(this, answers -> {
            if (answers == null) {
                // TODO fetch failed
                return;
            }
            adapter.addAnswers(answers);
            // Extract answer ids to fetch votes
            List<String> ids = new ArrayList<>();
            for (Answer answer : answers) {
                ids.add(answer.getId());
            }
            questionDetailViewModel.getAnswerVotes(ids);
        });
        questionDetailViewModel.getAnswerVoteLiveData().observe(this, stringVoteMap -> {
            if (stringVoteMap == null) {
                // TODO fetch failed
                return;
            }
            adapter.addAnswerVotes(stringVoteMap);
        });
    }

    public void createComment(@NonNull String postId, @NonNull String content) {
        commentViewModel.createComment(postId, content);
    }

    public void updateComments(@NonNull RecyclerView recyclerView, @NonNull String postId) {
        recyclerView.setAdapter(commentAdapter);
        commentViewModel.getComments(postId);

        commentViewModel.getCommentLiveData().observe(this, comments -> {
            commentAdapter.updateComments(comments);
        });
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
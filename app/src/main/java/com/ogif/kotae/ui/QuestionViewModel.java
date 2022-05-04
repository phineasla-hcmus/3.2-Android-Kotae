package com.ogif.kotae.ui;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.data.repository.UserRepository;
import com.ogif.kotae.data.repository.VoteCounterRepository;
import com.ogif.kotae.data.repository.VoteRepository;

import java.util.List;
import java.util.Objects;

public class QuestionViewModel extends ViewModel {

    private static final String TAG = "QuestionViewModel";
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    // Create new vote
    private final VoteRepository voteRepository;
    // Increment/decrement counter for both answer and question
    private final VoteCounterRepository voteCounterRepository;
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    public QuestionViewModel() {
        AuthRepository userRepository = new AuthRepository();
        String userId = Objects.requireNonNull(userRepository.getCurrentFirebaseUser()).getUid();

        this.voteRepository = new VoteRepository(userId);
        this.voteCounterRepository = new VoteCounterRepository();
        this.questionRepository = new QuestionRepository(this.voteRepository);
        this.userRepository = new UserRepository();
        this.mutableLiveData = new MutableLiveData<>();
    }

    public void createQuestion(@NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade,@NonNull List<String> imgIds) {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Fail");
            }

            @Override
            public void onSuccess(User result) {
                String authorId = result.getId();
                String authorName = result.getUsername();
                questionRepository.createQuestion(authorId, authorName, title, content, subjectId, gradeId, subject, grade,imgIds);
            }
        });
    }

    public LiveData<StateWrapper<Question>> getLiveData() {
        return mutableLiveData;
    }

    public void hideReport(ImageButton report, TextView counter) {
        userRepository.getCurrentUser(new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

            @Override
            public void onSuccess(User result) {
                String role = result.getRole();
                if (role != null && role.equals("admin")) {
                    report.setVisibility(View.VISIBLE);
                    counter.setVisibility(View.VISIBLE);
                } else {
                    report.setVisibility(View.INVISIBLE);
                    counter.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void updateVote(@NonNull Post holder, @Vote.State int previousState, @Vote.State int currentState) {
        Task<?> voteTask = null;
        Task<Void> counterTask;

        if (previousState == Vote.NONE && currentState == Vote.NONE) {
            Log.w(TAG, "updateVote: previousState and currentState shouldn't be NONE");
            return;
        }
        if (previousState == Vote.NONE) {
            boolean isUpvote = currentState == Vote.UPVOTE;
            voteTask = voteRepository.create(holder.getId(), isUpvote);
            counterTask = voteCounterRepository.increment(holder, isUpvote);
        } else {
            if (holder.getVoteId() == null) {
                Log.w(TAG, "updateVote: holder.getVoteId() == null, please recheck your logic");
                return;
            }
            voteTask = voteRepository.deleteById(holder.getVoteId());
            counterTask = voteCounterRepository.decrement(holder, previousState == Vote.UPVOTE);
        }
        Tasks.whenAllComplete(voteTask, counterTask).addOnSuccessListener(tasks -> {
            Task<?> voteResult = tasks.get(0);
            Task<?> counterResult = tasks.get(1);
            if (voteResult.isSuccessful() && counterResult.isSuccessful()) {
                @Nullable String voteId = (String) voteResult.getResult();
                holder.setVoteState(voteId, currentState);
            }
            if (!tasks.get(0).isSuccessful() && tasks.get(1).isSuccessful()) {
                // TODO revert counterTask
            } else if (!tasks.get(1).isSuccessful() && tasks.get(0).isSuccessful()) {
                // TODO revert voteTask
            }
        });}
}
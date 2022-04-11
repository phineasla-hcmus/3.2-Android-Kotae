package com.ogif.kotae.ui.questiondetail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.data.repository.VoteCounterRepository;
import com.ogif.kotae.data.repository.VoteRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuestionDetailViewModel extends ViewModel {
    private static final String TAG = "QuestionDetailViewModel";
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    // Create new vote
    private final VoteRepository voteRepository;
    // Increment/decrement counter for both answer and question
    private final VoteCounterRepository voteCounterRepository;
    private final MutableLiveData<Question> questionLiveData;
    private final MutableLiveData<Vote> questionVoteLiveData;
    private final MutableLiveData<List<Answer>> answerLiveData;
    private final MutableLiveData<Map<String, Vote>> answerVoteLiveData;

    public QuestionDetailViewModel() {
        AuthRepository userRepository = new AuthRepository();
        String userId = Objects.requireNonNull(userRepository.getCurrentFirebaseUser()).getUid();

        this.questionRepository = new QuestionRepository();
        this.answerRepository = new AnswerRepository();
        this.voteRepository = new VoteRepository(userId);
        this.voteCounterRepository = new VoteCounterRepository();

        this.questionLiveData = new MutableLiveData<>();
        this.questionVoteLiveData = new MutableLiveData<>();
        this.answerLiveData = new MutableLiveData<>();
        this.answerVoteLiveData = new MutableLiveData<>();
    }

    public QuestionDetailViewModel(Question question) {
        this();
        this.questionLiveData.setValue(question);
    }

    /**
     * Fetch question from Database, should only be used to check for update, otherwise use Question
     * passed from Home Activity
     */
    public void getQuestion(String id) {
        questionRepository.get(id, new TaskListener.State<Question>() {
            @Override
            public void onSuccess(@Nullable Question result) {
                questionLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                questionLiveData.postValue(null);
            }
        });
    }

    public void getAnswers() {
        String questionId = Objects.requireNonNull(questionLiveData.getValue()).getId();
        answerRepository.getListByQuestion(questionId, Global.QUERY_LIMIT, new TaskListener.State<List<Answer>>() {
            @Override
            public void onSuccess(List<Answer> result) {
                answerLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getAnswer(): " + e.getMessage());
                answerLiveData.postValue(null);
            }
        });
    }

    public void getQuestionVote(String id) {
        voteRepository.get(id, new TaskListener.State<Vote>() {
            @Override
            public void onSuccess(@Nullable Vote result) {
                questionVoteLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getQuestionVote(): " + e.getMessage());
                questionVoteLiveData.postValue(null);
            }
        });
    }

    public void setQuestionVote(@Vote.State int previousState, @Vote.State int currentState) {
        Question question = questionLiveData.getValue();
        Vote vote = questionVoteLiveData.getValue();
        if (question == null)
            return;
        // User hasn't voted yet, previousState should also be NONE, if not, recheck the logic
        if (vote == null) {
            // Vote.NONE is the same as vote == null (not existed on database) -> no change -> ignore
            if (currentState == Vote.NONE)
                return;
            // Create new vote on database
            Task<Void> voteTask = voteRepository.set(question.getId(), currentState);
            Task<Void> counterTask = voteCounterRepository.increment(question, currentState == Vote.UPVOTE);
            Tasks.whenAll(voteTask, counterTask).addOnSuccessListener(aVoid -> {
                // TODO do something
            }).addOnFailureListener(e -> {
                // TODO idk help me
            });
        }
        // Existing vote, just update it
        else {
            Task<Void> voteTask = voteRepository.set(vote, currentState);
            Task<Void> counterTask;
            if (currentState == Vote.NONE) {
                counterTask = voteCounterRepository.decrement(question, previousState == Vote.UPVOTE);
                Tasks.whenAll(voteTask, counterTask).addOnSuccessListener(aVoid -> {
                    questionVoteLiveData.setValue(null);
                });
            }
        }
    }

    public void setAnswerVote(Answer holder, @Vote.State int previousState, @Vote.State int currentState) {
        Question question = questionLiveData.getValue();
        Vote vote = questionVoteLiveData.getValue();
        if (question == null)
            return;
        if (vote == null) {
            if (currentState == Vote.NONE)
                return;
            if (currentState == Vote.UPVOTE) {
                // voteRepository.set
            }
        }
    }

    /**
     * Fetch all votes related to the user, the result will be in
     * {@link QuestionDetailViewModel#answerVoteLiveData}, if fetching data failed,
     * {@link QuestionDetailViewModel#answerVoteLiveData} will produce null
     */
    public void getAnswerVotes(List<String> recordIds) {
        voteRepository.getList(recordIds, new TaskListener.State<Map<String, Vote>>() {
            @Override
            public void onSuccess(Map<String, Vote> result) {
                answerVoteLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getAnswerVotes(): " + e.getMessage());
                answerVoteLiveData.postValue(null);
            }
        });
    }

    public LiveData<Question> getQuestionLiveData() {
        return questionLiveData;
    }

    public LiveData<Vote> getQuestionVoteLiveData() {
        return questionVoteLiveData;
    }

    public LiveData<List<Answer>> getAnswerLiveData() {
        return answerLiveData;
    }

    /**
     * A map of record ID and Vote that is in either state: Vote.UPVOTE or Vote.DOWNVOTE.
     * Caller should use Map.getOrDefault(Object, Object)
     */
    public LiveData<Map<String, Vote>> getAnswerVoteLiveData() {
        return answerVoteLiveData;
    }
}

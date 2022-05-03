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
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.data.repository.AnswerRepository;
import com.ogif.kotae.data.repository.AuthRepository;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.data.repository.VoteCounterRepository;
import com.ogif.kotae.data.repository.VoteRepository;
import com.ogif.kotae.utils.repository.OrderByVote;
import com.ogif.kotae.utils.repository.StartAfterVote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QuestionDetailViewModel extends ViewModel {
    private static final String TAG = "QuestionDetailViewModel";
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    // Create new vote
    private final VoteRepository voteRepository;
    // Increment/decrement counter for both answer and question
    private final VoteCounterRepository voteCounterRepository;

    /**
     * Store all posts
     */
    private final List<Post> posts;
    /**
     * Store latest fetched data from database, null indicates as initial value or query error,
     * while empty list indicates as successful but no data
     */
    private final MutableLiveData<List<? extends Post>> postsLiveData;

    public QuestionDetailViewModel() {
        AuthRepository userRepository = new AuthRepository();
        String userId = Objects.requireNonNull(userRepository.getCurrentFirebaseUser()).getUid();

        this.voteRepository = new VoteRepository(userId);
        this.voteCounterRepository = new VoteCounterRepository();
        this.questionRepository = new QuestionRepository(this.voteRepository);
        this.answerRepository = new AnswerRepository(this.voteRepository);

        this.posts = new ArrayList<>();
        this.posts.add(null);
        this.postsLiveData = new MutableLiveData<>();
    }

    public QuestionDetailViewModel(Question question) {
        this();
        posts.set(0, question);
        postsLiveData.setValue(getImmutableLocalPosts());

        getAnswers();
    }

    public void getAll(String questionId) {
        Task<Question> questionTask = questionRepository.getWithVote(questionId);
        Task<List<Answer>> answersTask = answerRepository.getListByQuestionWithVote(questionId, Global.QUERY_LIMIT, new OrderByVote());
        Tasks.whenAllSuccess(questionTask, answersTask).addOnSuccessListener(objects -> {
            Question question = (Question) objects.get(0);
            // I'm sure 100% that answersTask only returns a list of Answers.
            @SuppressWarnings("unchecked") List<Answer> answers = (List<Answer>) objects.get(1);
            if (!posts.isEmpty())
                posts.clear();
            posts.add(question);
            posts.addAll(answers);
            postsLiveData.postValue(getImmutableLocalPosts());
        }).addOnFailureListener(e -> {
            Log.w(TAG, "getAll: ", e);
            postsLiveData.postValue(null);
        });
    }

    public void getAnswers() {
        if (getLocalQuestion() == null) {
            Log.w(TAG, "getAnswers: no question");
            return;
        }
        String questionId = getLocalQuestion().getId();
        Task<List<Answer>> task = isAnswersEmpty() ?
                answerRepository.getListByQuestionWithVote(questionId, Global.QUERY_LIMIT, new OrderByVote()) :
                answerRepository.getListByQuestionWithVote(questionId, Global.QUERY_LIMIT, new StartAfterVote(getLastAnswer()));
        task.addOnSuccessListener(answers -> {
            posts.addAll(answers);
            postsLiveData.postValue(answers);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "getAnswers: ", e);
            postsLiveData.postValue(null);
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
        });
    }

    private List<Post> getLocalPosts() {
        return posts;
    }

    public List<Post> getImmutableLocalPosts() {
        return Collections.unmodifiableList(posts);
    }

    public LiveData<List<? extends Post>> getPostsLiveData() {
        return postsLiveData;
    }

    @Nullable
    public Question getLocalQuestion() {
        return (Question) posts.get(0);
    }

    @Nullable
    public Answer getLastAnswer() {
        return isAnswersEmpty() ? null : (Answer) posts.get(posts.size() - 1);
    }

    public boolean isQuestionEmpty() {
        return posts.isEmpty();
    }

    public boolean isAnswersEmpty() {
        return posts.size() <= 1;
    }

}

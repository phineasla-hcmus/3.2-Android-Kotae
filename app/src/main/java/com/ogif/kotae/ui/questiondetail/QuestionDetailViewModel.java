package com.ogif.kotae.ui.questiondetail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
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

    private final List<Post> posts;
    // null indicates as initial value or query error
    // Empty list indicates as successful but no data
    private final MutableLiveData<List<Post>> postLiveData;
    private final MutableLiveData<Boolean> stateLiveData;
    // private final MutableLiveData<Question> questionMutableLiveData;
    // private final MutableLiveData<List<Answer>> answerMutableLiveData;

    public QuestionDetailViewModel() {
        AuthRepository userRepository = new AuthRepository();
        String userId = Objects.requireNonNull(userRepository.getCurrentFirebaseUser()).getUid();

        this.voteRepository = new VoteRepository(userId);
        this.voteCounterRepository = new VoteCounterRepository();
        this.questionRepository = new QuestionRepository(this.voteRepository);
        this.answerRepository = new AnswerRepository(this.voteRepository);

        this.posts = new ArrayList<>();
        this.postLiveData = new MutableLiveData<>();
        this.stateLiveData = new MutableLiveData<>();
    }

    public QuestionDetailViewModel(Question question) {
        this();
        List<Post> posts = getLocalPosts();
        posts.add(question);
        this.postLiveData.setValue(posts);
    }

    public void getAll(String questionId) {
        Task<Question> questionTask = questionRepository.getWithVote(questionId);
        Task<List<Answer>> answersTask = answerRepository.getListByQuestionWithVote(questionId, Global.QUERY_LIMIT, new OrderByVote());
        Tasks.whenAllSuccess(questionTask, answersTask).addOnSuccessListener(objects -> {
            Question question = (Question) objects.get(0);
            // I'm sure 100% that answersTask only returns a list of Answers.
            @SuppressWarnings("unchecked") List<Answer> answers = (List<Answer>) objects.get(1);
            
        }).addOnFailureListener(e -> stateLiveData.postValue(false));
    }

    public void getAnswers() {
        List<Post> posts = postLiveData.getValue();
        assert posts != null : "postLiveData should never be null";
        String questionId = posts.get(0).getId();
        // answerRepository.getListByQuestion(questionId, Global.QUERY_LIMIT, new TaskListener.State<List<Answer>>() {
        //     @Override
        //     public void onSuccess(List<Answer> result) {
        //         List<Post> posts = postLiveData.getValue();
        //         assert posts != null : "postLiveData should never be null";
        //         posts.addAll(result);
        //         postLiveData.postValue(posts);
        //     }
        //
        //     @Override
        //     public void onFailure(@NonNull Exception e) {
        //         Log.w(TAG, "getAnswer(): Failed with" + e.getMessage());
        //     }
        // });
    }

    public void updateVote(@NonNull Post holder, int position, @Vote.State int previousState, @Vote.State int currentState) {
        Task<?> voteTask = null;
        Task<Void> counterTask;

        if (previousState == Vote.NONE && currentState == Vote.NONE) {
            Log.w(TAG, "updateVote: previousState and currentState should not all NONE");
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
            voteTask = voteRepository.delete(holder.getVoteId());
            counterTask = voteCounterRepository.decrement(holder, previousState == Vote.UPVOTE);
        }
        Tasks.whenAllComplete(voteTask, counterTask).addOnSuccessListener(tasks -> {
            Task<?> voteResult = tasks.get(0);
            Task<?> counterResult = tasks.get(1);
            if (voteResult.isSuccessful() && counterResult.isSuccessful()) {
                @Nullable String voteId = (String) voteResult.getResult();
                Post post = getLocalPosts().get(position);
                post.setVoteState(voteId, currentState);
            }
            if (!tasks.get(0).isSuccessful() && tasks.get(1).isSuccessful()) {
                // TODO revert counterTask
            } else if (!tasks.get(1).isSuccessful() && tasks.get(0).isSuccessful()) {
                // TODO revert voteTask
            }
        });
    }

    public Question getLocalQuestion() {
        return (Question) getLocalPosts().get(0);
    }

    public List<Post> getLocalPosts() {
        return Collections.unmodifiableList(posts);
    }

    public LiveData<List<Post>> getPostLiveData() {
        return postLiveData;
    }
}

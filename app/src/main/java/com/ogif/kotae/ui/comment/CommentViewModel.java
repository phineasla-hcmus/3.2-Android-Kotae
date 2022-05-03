package com.ogif.kotae.ui.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.data.repository.CommentRepository;
import com.ogif.kotae.data.repository.VoteCounterRepository;
import com.ogif.kotae.data.repository.VoteRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final VoteCounterRepository voteCounterRepository;

    private final List<Comment> comments;
    // null indicates as initial value or query error
    // Empty list indicates as successful but no data
    private final MutableLiveData<List<Comment>> commentLiveData;

    public CommentViewModel(String userId, String username, String postId) {
        this.commentRepository = new CommentRepository(userId, username, postId);
        this.voteRepository = new VoteRepository(userId);
        this.voteCounterRepository = new VoteCounterRepository();

        this.comments = new ArrayList<>();
        this.commentLiveData = new MutableLiveData<>();
    }

    public void getComments() {
        Task<List<Comment>> task;
        if (comments.isEmpty())
            task = commentRepository.getListByParentWithVote(Global.QUERY_LIMIT);
            // task = commentRepository.getListByParentWithVotes(Global.QUERY_LIMIT, new OrderByDate());
        else {
            Date lastDate = comments.get(comments.size() - 1).getPostTime();
            task = commentRepository.getListByParentWithVoteAfter(lastDate, Global.QUERY_LIMIT);
            // task = commentRepository.getListByParentWithVotes(Global.QUERY_LIMIT, new StartAfterDate(lastDate));
        }
        task.addOnSuccessListener(result -> {
            if (result.isEmpty())
                commentLiveData.postValue(result);
            else {
                comments.addAll(result);
                commentLiveData.postValue(comments);
            }
        }).addOnFailureListener(e -> {
            Log.w(TAG, "getComments: ", e);
            commentLiveData.postValue(null);
        });
    }

    public void createComment(@NonNull String content) {
        commentRepository.create(content).addOnSuccessListener(comment -> {
            comments.add(comment);
            commentLiveData.postValue(Collections.singletonList(comment));
            // TODO update counter
        });
    }

    public void updateVote(@NonNull Comment holder, @Vote.State int previousState, @Vote.State int currentState) {
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
        });
    }

    public String getPostId() {
        return commentRepository.getPostId();
    }

    public List<Comment> getImmutableLocalComments() {
        return Collections.unmodifiableList(comments);
    }

    public LiveData<List<Comment>> getCommentLiveData() {
        return commentLiveData;
    }
}

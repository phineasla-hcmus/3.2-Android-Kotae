package com.ogif.kotae.ui.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.repository.CommentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
    private final List<Comment> comments;
    // null indicates as initial value or query error
    // Empty list indicates as query successful but no comment
    private final MutableLiveData<List<Comment>> commentLiveData;

    public CommentViewModel(String userId, String username, String postId) {
        this.commentRepository = new CommentRepository(userId, username, postId);
        this.comments = new ArrayList<>();
        this.commentLiveData = new MutableLiveData<>();
    }

    public void getComments() {
        Task<List<Comment>> task;
        if (comments.isEmpty())
            task = commentRepository.getListWithVotes(Global.QUERY_LIMIT);
        else {
            Date lastDate = comments.get(comments.size() - 1).getPostTime();
            // Log.d(TAG, "getComments lastId: " + lastId);
            task = commentRepository.getListWithVotesAfter(lastDate, Global.QUERY_LIMIT);
        }
        task.addOnSuccessListener(result -> {
            if (result.isEmpty())
                commentLiveData.postValue(result);
            else {
                comments.addAll(result);
                commentLiveData.postValue(comments);
            }
        }).addOnFailureListener(e -> {
            Log.w(TAG, "getComments: Failed with " + e.getMessage());
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

    public String getPostId() {
        return commentRepository.getPostId();
    }

    public List<Comment> getLocalComments() {
        return Collections.unmodifiableList(comments);
    }

    public LiveData<List<Comment>> getCommentLiveData() {
        return commentLiveData;
    }
}

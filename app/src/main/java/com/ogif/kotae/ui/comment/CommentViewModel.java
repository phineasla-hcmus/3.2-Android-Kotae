package com.ogif.kotae.ui.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.repository.CommentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
    // null indicates as initial value or query error
    // Empty list indicates as query successful but no comment
    private final MutableLiveData<List<Comment>> commentLiveData;

    public CommentViewModel(String userId, String username, String postId) {
        this.commentRepository = new CommentRepository(userId, username, postId);
        this.commentLiveData = new MutableLiveData<>();
    }

    public void createComment(@NonNull String content) {
        // userRepository.getCurrentUser(new TaskListener.State<User>() {
        //     @Override
        //     public void onSuccess(User result) {
        //         String authorId = result.getId();
        //         String authorName = result.getUsername();
        //         commentRepository.createComment(postId, authorId, authorName, content);
        //     }
        //
        //     @Override
        //     public void onFailure(@NonNull Exception e) {
        //         Log.d(TAG, "Fail");
        //     }
        // });

        commentRepository.create(content).addOnSuccessListener(comment -> {
            List<Comment> comments = commentLiveData.getValue();
            if (comments == null)
                comments = new ArrayList<>();
            comments.add(comment);
            commentLiveData.postValue(comments);
        });
    }

    public void getComments() {
        final List<Comment> comments = commentLiveData.getValue();
        Task<List<Comment>> task;
        if (comments == null || comments.isEmpty())
            task = commentRepository.getListWithVotes(Global.QUERY_LIMIT);
        else {
            String lastId = comments.get(comments.size() - 1).getId();
            task = commentRepository.getListWithVotesAfter(lastId, Global.QUERY_LIMIT);
        }
        task.addOnSuccessListener(result -> {
            if (comments == null)
                commentLiveData.postValue(new ArrayList<>(result));
            else {
                comments.addAll(result);
                commentLiveData.postValue(comments);
            }
        }).addOnFailureListener(e -> {
            Log.w(TAG, "getComments: Failed with " + e.getMessage());
            commentLiveData.postValue(commentLiveData.getValue());
        });
    }

    public String getPostId() {
        return commentRepository.getPostId();
    }

    public LiveData<List<Comment>> getCommentLiveData() {
        return commentLiveData;
    }
}

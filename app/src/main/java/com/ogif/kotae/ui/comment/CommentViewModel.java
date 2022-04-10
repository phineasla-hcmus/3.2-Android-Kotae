package com.ogif.kotae.ui.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.CommentRepository;
import com.ogif.kotae.data.repository.UserRepository;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
    private final MutableLiveData<List<Comment>> commentLiveData;

    public CommentViewModel(String userId, String username) {
        this.commentRepository = new CommentRepository(userId, username);
        this.commentLiveData = new MutableLiveData<>();
    }

    public void createComment(@NonNull String postId, @NonNull String content) {
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

        // commentRepository.createComment()
    }

    public void getComments(String postId) {
        commentRepository.getList(postId, Global.QUERY_LIMIT, new TaskListener.State<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> result) {
                commentLiveData.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed to getComments()");
                commentLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<Comment>> getCommentLiveData() {
        return commentLiveData;
    }
}

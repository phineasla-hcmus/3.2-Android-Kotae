package com.ogif.kotae.ui.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.repository.CommentRepository;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
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

        // commentRepository.createComment()
    }

    public void getComments() {
        commentRepository.getListWithVotes(Global.QUERY_LIMIT)
                .addOnSuccessListener(commentLiveData::postValue)
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Failed to getComments()");
                    commentLiveData.postValue(null);
                });
    }

    public String getPostId() {
        return commentRepository.getPostId();
    }

    public LiveData<List<Comment>> getCommentLiveData() {
        return commentLiveData;
    }
}

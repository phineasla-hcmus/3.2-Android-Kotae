package com.ogif.kotae.ui.comment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.repository.CommentPagingSource;
import com.ogif.kotae.data.repository.CommentRepository;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
    private final MutableLiveData<List<Comment>> commentLiveData;
    private final LiveData<PagingData<Comment>> pagingCommentLiveData;

    public CommentViewModel(String userId, String username, String postId) {
        this.commentRepository = new CommentRepository(userId, username, postId);
        this.commentLiveData = new MutableLiveData<>();

        CommentPagingSource pagingSource = new CommentPagingSource(commentRepository);
        Pager<String, Comment> pager = new Pager<>
                (new PagingConfig(Global.QUERY_LIMIT), () -> pagingSource);

        pagingCommentLiveData = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager),
                ViewModelKt.getViewModelScope(this));
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
            if (comments != null) {
                comments.add(comment);
                commentLiveData.postValue(comments);
            }
        });
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

    public LiveData<PagingData<Comment>> getPagingCommentLiveData() {
        return pagingCommentLiveData;
    }
}

package com.ogif.kotae.ui.comment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.repository.CommentPagingSource;
import com.ogif.kotae.data.repository.CommentRepository;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "CommentViewModel";
    private final CommentRepository commentRepository;
    private final LiveData<PagingData<Comment>> pagingCommentLiveData;

    public CommentViewModel(String userId, String username, String postId) {
        this.commentRepository = new CommentRepository(userId, username, postId);

        CommentPagingSource pagingSource = new CommentPagingSource(commentRepository);
        Pager<String, Comment> pager = new Pager<>
                (new PagingConfig(Global.QUERY_LIMIT), () -> pagingSource);

        this.pagingCommentLiveData = PagingLiveData.getLiveData(pager);

        // pagingCommentLiveData = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager),
        //         ViewModelKt.getViewModelScope(this));
        PagingLiveData.cachedIn(pagingCommentLiveData, this);
    }

    public void createComment(@NonNull String content) {
        commentRepository.create(content).addOnSuccessListener(comment -> {
        });
    }

    public void getComments() {
        // commentRepository.getListWithVotes(Global.QUERY_LIMIT)
        //         .addOnSuccessListener(commentLiveData::postValue)
        //         .addOnFailureListener(e -> {
        //             Log.w(TAG, "Failed to getComments()");
        //             commentLiveData.postValue(null);
        //         });
    }

    public String getPostId() {
        return commentRepository.getPostId();
    }

    public LiveData<PagingData<Comment>> getPagingCommentLiveData() {
        return pagingCommentLiveData;
    }
}

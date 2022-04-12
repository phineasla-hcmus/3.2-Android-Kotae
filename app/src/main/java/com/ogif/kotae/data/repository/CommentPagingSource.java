package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.ogif.kotae.data.model.Comment;

import java.util.ArrayList;

public class CommentPagingSource extends ListenableFuturePagingSource<String, Comment> {
    @NonNull
    private final CommentRepository commentRepository;
    @NonNull
    private String postId;

    public CommentPagingSource(@NonNull CommentRepository commentRepository, @NonNull String postId) {
        this.commentRepository = commentRepository;
        this.postId = postId;
    }

    public CommentPagingSource(@NonNull String authorId, @NonNull String authorName, @NonNull String postId) {
        this.commentRepository = new CommentRepository(authorId, authorName, postId);
        this.postId = postId;
    }

    @Nullable
    @Override
    public String getRefreshKey(@NonNull PagingState<String, Comment> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<String, Comment>> loadFuture(@NonNull LoadParams<String> loadParams) {
        String nextPageId = loadParams.getKey();


        return null;
    }

    private LoadResult<String, Comment> toLoadResult() {
        return new LoadResult.Page<String, Comment>(new ArrayList<Comment>(), null, "abc", LoadResult.Page.COUNT_UNDEFINED, LoadResult.Page.COUNT_UNDEFINED);
    }
}

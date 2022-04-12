package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.Query;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.Record;

import java.util.ArrayList;

public class CommentPagingSource extends ListenableFuturePagingSource<String, Comment> {
    @NonNull
    private final CommentRepository commentRepository;
    @NonNull
    private Query query;

    public CommentPagingSource(@NonNull CommentRepository commentRepository, @NonNull Query query) {
        this.commentRepository = commentRepository;
        this.query = query;
    }

    public CommentPagingSource(@NonNull String authorId, @NonNull String authorName, @NonNull Query query) {
        this.commentRepository = new CommentRepository(authorId, authorName);
        this.query = query;
    }

    @Nullable
    @Override
    public String getRefreshKey(@NonNull PagingState<String, Comment> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<String, Comment>> loadFuture(@NonNull LoadParams<String> loadParams) {
        return null;
    }

    private LoadResult<String, Comment> toLoadResult() {
        return new LoadResult.Page<String, Comment>(new ArrayList<Comment>(), null, "abc", LoadResult.Page.COUNT_UNDEFINED, LoadResult.Page.COUNT_UNDEFINED);
    }
}

package com.ogif.kotae.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.ogif.kotae.data.model.Comment;

import java.util.List;

public class CommentPagingSource extends ListenableFuturePagingSource<String, Comment> {
    @NonNull
    private final CommentRepository commentRepository;

    public CommentPagingSource(@NonNull CommentRepository commentRepository, @NonNull String postId) {
        this.commentRepository = commentRepository;
    }

    public CommentPagingSource(@NonNull String authorId, @NonNull String authorName, @NonNull String postId) {
        this.commentRepository = new CommentRepository(authorId, authorName, postId);
    }

    /**
     * @see <a href="https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data">
     * Paging Library
     * </a>
     * @see <a href="https://developer.android.com/reference/kotlin/androidx/paging/ListenableFuturePagingSource">
     * ListenableFuturePagingSource
     * </a>
     */
    @Nullable
    @Override
    public String getRefreshKey(@NonNull PagingState<String, Comment> state) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition != null) {
            Comment comment = state.closestItemToPosition(anchorPosition);
            return comment != null ? comment.getId() : null;
        }
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<String, Comment>> loadFuture(@NonNull LoadParams<String> loadParams) {
        String nextPageId = loadParams.getKey();
        Task<List<Comment>> task = nextPageId == null
                ? commentRepository.getListWithVotes(loadParams.getLoadSize())
                : commentRepository.getListWithVotesAfter(nextPageId, loadParams.getLoadSize());

        return CallbackToFutureAdapter.getFuture(completer -> task.addOnCompleteListener(completedTask -> {
            if (completedTask.isCanceled()) {
                completer.setCancelled();
            } else if (completedTask.isSuccessful()) {
                List<Comment> result = completedTask.getResult();
                if (result != null)
                    completer.set(toLoadResult(completedTask.getResult()));
                else
                    throw new IllegalStateException();
            } else {
                Exception e = completedTask.getException();
                if (e != null)
                    completer.set(new LoadResult.Error<>(completedTask.getException()));
                else
                    throw new IllegalStateException();
            }
        }));
    }

    private LoadResult<String, Comment> toLoadResult(List<Comment> result) {
        String lastKey = result.get(result.size() - 1).getId();
        return new LoadResult.Page<>(result, null, lastKey, LoadResult.Page.COUNT_UNDEFINED, LoadResult.Page.COUNT_UNDEFINED);
    }
}

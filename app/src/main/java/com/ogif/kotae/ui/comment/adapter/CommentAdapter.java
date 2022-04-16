package com.ogif.kotae.ui.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.ui.VoteView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends PagingDataAdapter<Comment, RecyclerView.ViewHolder> {
    private final Context context;

    public CommentAdapter(@NonNull Context context, @NonNull DiffUtil.ItemCallback<Comment> diffCallback) {
        super(diffCallback);
        this.context = context;
    }

    private static class CommentHolder extends RecyclerView.ViewHolder {
        private final TextView username, content;
        private final CircleImageView avatar;
        private final VoteView vote;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.tv_comment_avatar);
            this.username = itemView.findViewById(R.id.tv_comment_username);
            this.content = itemView.findViewById(R.id.tv_comment_content);
            this.vote = itemView.findViewById(R.id.tv_comment_vote);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CommentHolder(inflater
                .inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = getItem(position);
        if (comment == null)
            return;

        CommentHolder commentHolder = (CommentHolder) holder;
        commentHolder.avatar.setImageResource(R.drawable.ic_placeholder_user);
        commentHolder.content.setText(comment.getContent());
        commentHolder.username.setText(comment.getAuthor());
        commentHolder.vote.setVoteState(comment.getUpvote(), comment.getDownvote(), comment.getVoteState());
    }
}

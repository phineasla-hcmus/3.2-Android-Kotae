package com.ogif.kotae.ui.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.Global;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.ui.common.adapter.RecordAdapter;
import com.ogif.kotae.ui.common.view.VoteView;
import com.ogif.kotae.utils.model.CommentUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecordAdapter<Comment> {

    public CommentAdapter(Context context) {
        super(context);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Comment comment = items.get(position);

        CommentHolder holder = (CommentHolder) viewHolder;
        holder.content.setText(comment.getContent());
        holder.username.setText(comment.getAuthor());
        holder.vote.setHolder(comment);
        holder.vote.setVoteState(comment.getUpvote(), comment.getDownvote(), comment.getVoteState());
        holder.vote.setOnStateChangeListener(new VoteView.DebouncedOnStateChangeListener(Global.DEBOUNCE_MILLIS) {
            @Override
            public void onDebouncedStateChange(VoteView view, int previous, int current) {
                onVoteChangeListenerIfNotNull(viewHolder.getBindingAdapterPosition(), view, previous, current);
            }
        });
        // TODO user avatar
        holder.avatar.setImageResource(R.drawable.ic_placeholder_user);
    }

    @Override
    public void setItems(@NonNull List<Comment> items) {
        setItems(items, new CommentUtils.ListComparator(items, this.items));
    }
}

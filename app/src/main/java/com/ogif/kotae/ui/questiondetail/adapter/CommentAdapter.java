package com.ogif.kotae.ui.questiondetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Comment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Comment> comments;
    private final Context context;

    public CommentAdapter(Context context) {
        this.context = context;
        this.comments = new ArrayList<>();
    }

    private static class CommentHolder extends RecyclerView.ViewHolder {
        private final TextView username, content;
        private final CircleImageView avatar;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.tv_comment_avatar);
            this.username = itemView.findViewById(R.id.tv_comment_username);
            this.content = itemView.findViewById(R.id.tv_comment_content);
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
        Comment comment = comments.get(position);

        CommentHolder commentHolder = (CommentHolder) holder;
        commentHolder.avatar.setImageResource(R.drawable.ic_placeholder_user);
        commentHolder.content.setText(comment.getContent());
        commentHolder.username.setText(comment.getAuthor());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void updateComments(@NonNull List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }
}

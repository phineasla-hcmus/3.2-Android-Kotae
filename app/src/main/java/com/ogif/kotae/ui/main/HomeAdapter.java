package com.ogif.kotae.ui.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.question.QuestionDetailActivity;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Question> questionList;
    private Context context;

    public HomeAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.content.setText(question.getContent());
        // holder.postTime.setText((int) questionList.get(position).getPostTime());
        // TODO support avatar
        holder.avatar.setImageResource(R.drawable.ic_baseline_account_circle);
        holder.author.setText(question.getAuthorId());
        holder.title.setText(question.getTitle());
        holder.layout.setOnClickListener(view -> {
            Intent intent = QuestionDetailActivity.newInstance(context, question);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content, author, postTime;
        ImageButton upvote, downvote, report;
        CircleImageView avatar;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.tv_question_content);
            title = (TextView) itemView.findViewById(R.id.tv_question_title);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            postTime = (TextView) itemView.findViewById(R.id.tv_question_post_time);

            upvote = (ImageButton) itemView.findViewById(R.id.ib_up);
            downvote = (ImageButton) itemView.findViewById(R.id.ib_down);
            report = (ImageButton) itemView.findViewById(R.id.ib_report);

            avatar = (CircleImageView) itemView.findViewById(R.id.cim_avatar);

            layout = (ConstraintLayout) itemView.findViewById(R.id.constraint_layout_question);
        }
    }
}


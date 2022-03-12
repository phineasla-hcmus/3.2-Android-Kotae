package com.ogif.kotae.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<Question> questionList;
    private Context context;

    public QuestionAdapter(List<Question> questionList, Context context) {
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
        holder.tv_question_content.setText(questionList.get(position).getContent());
//        holder.tv_question_posttime.setText((int) questionList.get(position).getPostTime());
        holder.cim_avatar.setImageResource(R.drawable.ic_baseline_account_circle);
        holder.tv_author.setText(questionList.get(position).getAuthorId());
        holder.tv_question_title.setText(questionList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_question_title, tv_question_content, tv_author, tv_question_posttime;
        ImageButton ib_upvote, ib_downvote, ib_report;
        CircleImageView cim_avatar;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_question_content = (TextView) itemView.findViewById(R.id.tv_question_content);
            tv_question_title = (TextView) itemView.findViewById(R.id.tv_question_title);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_question_posttime = (TextView) itemView.findViewById(R.id.tv_question_post_time);

            ib_upvote = (ImageButton) itemView.findViewById(R.id.ib_up);
            ib_downvote = (ImageButton) itemView.findViewById(R.id.ib_down);
            ib_report = (ImageButton) itemView.findViewById(R.id.ib_report);

            cim_avatar = (CircleImageView) itemView.findViewById(R.id.cim_avatar);
        }
    }
}


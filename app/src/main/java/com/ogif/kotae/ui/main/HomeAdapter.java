package com.ogif.kotae.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.UserRepository;
import com.ogif.kotae.ui.question.QuestionDetailActivity;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends FirestoreRecyclerAdapter<Question,HomeAdapter.ViewHolder> {
//    private List<Question> questionList;
//    private Context context;
//
//    public HomeAdapter(List<Question> questionList, Context context) {
//        this.questionList = questionList;
//        this.context = context;
//    }

    public HomeAdapter(@NonNull FirestoreRecyclerOptions<Question> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Question model) {
        Log.d("question: ", model.getAuthorName() + " " + model.getAuthorId());
        holder.content.setText(model.getContent());
        // holder.postTime.setText((int) questionList.get(position).getPostTime());
        // TODO support avatar
        holder.avatar.setImageResource(R.drawable.ic_baseline_account_circle);
        holder.author.setText(model.getAuthorName());
        holder.title.setText(model.getTitle());
        holder.tv_up.setText(Integer.toString(model.getUpvote()));
        holder.tv_down.setText(Integer.toString(model.getDownvote()));
        holder.tv_report.setText(Integer.toString(model.getReport()));
        holder.subject.setText(model.getSubject());
        holder.grade.setText(model.getGrade());
        holder.layout.setOnClickListener(view -> {
//            Intent intent = QuestionDetailActivity.newInstance(holder.layout.getContext(), model);
//            view.getContext().startActivity(intent);
            Log.d("model", "pos "+String.valueOf(position)+": " +model.getAuthorId());
        });
    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        UserRepository userRepository = new UserRepository();
//        Question question = questionList.get(position);
//        holder.content.setText(question.getContent());
//        // holder.postTime.setText((int) questionList.get(position).getPostTime());
//        // TODO support avatar
//        holder.avatar.setImageResource(R.drawable.ic_baseline_account_circle);
//        holder.author.setText(question.getAuthorId());
//        holder.title.setText(question.getTitle());
//        holder.layout.setOnClickListener(view -> {
//            Intent intent = QuestionDetailActivity.newInstance(context, question);
//            context.startActivity(intent);
//        });
//    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content, author, postTime, tv_up, tv_down, tv_report;
        ImageButton upvote, downvote, report;
        Chip grade, subject;
        CircleImageView avatar;
        ConstraintLayout layout;
        boolean downClicked, upClicked;

        public ViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.tv_question_content);
            title = (TextView) itemView.findViewById(R.id.tv_question_title);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            postTime = (TextView) itemView.findViewById(R.id.tv_question_post_time);
            tv_up = (TextView) itemView.findViewById(R.id.tv_up);
            tv_down = (TextView) itemView. findViewById(R.id.tv_down);
            tv_report = (TextView) itemView.findViewById(R.id.tv_report);

            upvote = (ImageButton) itemView.findViewById(R.id.ib_up);
            downvote = (ImageButton) itemView.findViewById(R.id.ib_down);
            report = (ImageButton) itemView.findViewById(R.id.ib_report);

            avatar = (CircleImageView) itemView.findViewById(R.id.cim_avatar);

            layout = (ConstraintLayout) itemView.findViewById(R.id.constraint_layout_question);

            grade = (Chip) itemView.findViewById(R.id.chip_grade);
            subject = (Chip) itemView.findViewById(R.id.chip_subject);

            downClicked = false;
            upClicked = false;

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!upClicked)
                    {
                        upvote.setImageResource(R.drawable.ic_baseline_arrow_upward_selected);
                        upClicked = true;
                        downClicked = false;
                        downvote.setImageResource(R.drawable.ic_baseline_arrow_downward);
                        //cập nhật lại số lượng
                    }
                    else
                    {
                        upvote.setImageResource(R.drawable.ic_baseline_arrow_upward);
                        upClicked = false;
                        //cập nhật số lượng
                    }
                }
            });
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!downClicked)
                    {
                        downvote.setImageResource(R.drawable.ic_baseline_arrow_downward_selected);
                        downClicked = true;
                        upClicked = false;
                        upvote.setImageResource(R.drawable.ic_baseline_arrow_upward);
                        //cập nhật lại số lượng
                    }
                    else
                    {
                        downvote.setImageResource(R.drawable.ic_baseline_arrow_downward);
                        downClicked = false;
                        //cập nhật số lượng
                    }
                }
            });
        }
    }
}


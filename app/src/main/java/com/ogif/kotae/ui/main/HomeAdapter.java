package com.ogif.kotae.ui.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.question.QuestionDetailActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends FirestoreRecyclerAdapter<Question, HomeAdapter.ViewHolder> {
    private final Context context;

    public HomeAdapter(@NonNull FirestoreRecyclerOptions<Question> options, @NonNull Context context) {
        super(options);
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
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Question model) {
        Log.d("question: ", model.getAuthor() + " " + model.getAuthorId());
        holder.content.setText(model.getContent());
        // holder.postTime.setText((int) questionList.get(position).getPostTime());
        // TODO support avatar
        holder.avatar.setImageResource(R.drawable.ic_baseline_account_circle);

        holder.author.setText(model.getAuthor());
        holder.title.setText(model.getTitle());
        holder.upvoteCounter.setText(Integer.toString(model.getUpvote()));
        holder.downvoteCounter.setText(Integer.toString(model.getDownvote()));
        holder.reportCounter.setText(Integer.toString(model.getReport()));
        holder.subject.setText(model.getSubject());
        holder.grade.setText(model.getGrade());
        holder.layout.setOnClickListener(view -> {
            Intent intent = QuestionDetailActivity.newInstance(context, model);
            context.startActivity(intent);
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

    private void startQuestionDetailActivity(@NonNull Question question) {
        Intent intent = QuestionDetailActivity.newInstance(context, question);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content, author, postTime, upvoteCounter, downvoteCounter, reportCounter;
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
            upvoteCounter = (TextView) itemView.findViewById(R.id.tv_up);
            downvoteCounter = (TextView) itemView.findViewById(R.id.tv_down);
            reportCounter = (TextView) itemView.findViewById(R.id.tv_report);

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
                    if (!upClicked) {
                        upvote.setImageResource(R.drawable.ic_baseline_arrow_upward_selected);
                        upClicked = true;
                        downClicked = false;
                        downvote.setImageResource(R.drawable.ic_baseline_arrow_downward);
                        //cập nhật lại số lượng
                    } else {
                        upvote.setImageResource(R.drawable.ic_baseline_arrow_upward);
                        upClicked = false;
                        //cập nhật số lượng
                    }
                }
            });
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!downClicked) {
                        downvote.setImageResource(R.drawable.ic_baseline_arrow_downward_selected);
                        downClicked = true;
                        upClicked = false;
                        upvote.setImageResource(R.drawable.ic_baseline_arrow_upward);
                        //cập nhật lại số lượng
                    } else {
                        downvote.setImageResource(R.drawable.ic_baseline_arrow_downward);
                        downClicked = false;
                        //cập nhật số lượng
                    }
                }
            });
        }
    }
}


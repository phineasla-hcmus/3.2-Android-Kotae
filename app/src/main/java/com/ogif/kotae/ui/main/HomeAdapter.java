package com.ogif.kotae.ui.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.ui.QuestionViewModel;
import com.ogif.kotae.ui.common.view.VerticalVoteView;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;
import com.ogif.kotae.utils.DateUtils;
import com.ogif.kotae.utils.text.MarkdownUtils;

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
        //holder.content.setText(model.getContent());
        holder.report.setVisibility(View.INVISIBLE);
        holder.reportCounter.setVisibility(View.INVISIBLE);
        MarkdownUtils.setMarkdown(context, model.getContent(), holder.content);
        // holder.postTime.setText((int) questionList.get(position).getPostTime());
        holder.postTime.setText(DateUtils.formatDate(model.getPostTime(), context));
        // TODO support avatar
        holder.avatar.setImageResource(R.drawable.ic_baseline_account_circle);

        holder.author.setText(model.getAuthor());
        holder.title.setText(model.getTitle());
//        holder.upvoteCounter.setText(Integer.toString(model.getUpvote()));
//        holder.downvoteCounter.setText(Integer.toString(model.getDownvote()));
        holder.reportCounter.setText(Integer.toString(model.getReport()));
        holder.subject.setText(model.getSubject());
        holder.grade.setText(model.getGrade());
        holder.layout.setOnClickListener(view -> {
            Intent intent = QuestionDetailActivity.newInstance(context, model);
            context.startActivity(intent);
        });
        QuestionRepository questionRepository= new QuestionRepository();
//        holder.upvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!holder.upClicked) {
//                   // holder.upvote.setImageResource(R.drawable.ic_baseline_arrow_upward_selected);
//                    holder.upClicked = true;
//                    holder.downClicked = false;
//                   // holder.downvote.setImageResource(R.drawable.ic_baseline_arrow_downward);
//                    Log.d("DATA", model.getId());
//                    questionRepository.updateUpvote(model.getId());
//                } else {
//                   // holder.upvote.setImageResource(R.drawable.ic_baseline_arrow_upward);
//                    holder.upClicked = false;
//
//                }
//            }
//        });
//        holder.downvote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!holder.downClicked) {
//                    //holder.downvote.setImageResource(R.drawable.ic_baseline_arrow_downward_selected);
//                    holder.downClicked = true;
//                    holder.upClicked = false;
//                    //holder.upvote.setImageResource(R.drawable.ic_baseline_arrow_upward);
//                    //cập nhật lại số lượng
//                } else {
//                    //holder.downvote.setImageResource(R.drawable.ic_baseline_arrow_downward);
//                    holder.downClicked = false;
//                    //cập nhật số lượng
//                }
//            }
//        });
    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        AuthRepository userRepository = new AuthRepository();
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

        TextView title, content, author, postTime, upvoteCounter, downvoteCounter, reportCounter;
        ImageButton  report;
        Button upvote, downvote;
        VerticalVoteView verticalVoteView;
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
//            upvoteCounter = (TextView) itemView.findViewById(R.id.tv_up);
//            downvoteCounter = (TextView) itemView.findViewById(R.id.tv_down);
            reportCounter = (TextView) itemView.findViewById(R.id.tv_report);
                verticalVoteView = (VerticalVoteView) itemView.findViewById(R.id.verticalVoteView);
//            upvote = (Button) itemView.findViewById(R.id.ib_up);
//            downvote = (Button) itemView.findViewById(R.id.ib_down);
            report = (ImageButton) itemView.findViewById(R.id.ib_report);

            avatar = (CircleImageView) itemView.findViewById(R.id.cim_avatar);

            layout = (ConstraintLayout) itemView.findViewById(R.id.constraint_layout_question);

            grade = (Chip) itemView.findViewById(R.id.chip_grade);
            subject = (Chip) itemView.findViewById(R.id.chip_subject);



            QuestionViewModel questionViewModel = new QuestionViewModel();
            questionViewModel.hideReport(report, reportCounter);

        }
    }
}


package com.ogif.kotae.ui.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.firebase.Timestamp;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.NotificationRepository;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;

import java.util.ArrayList;


public class AdminQuestionAdapter extends BaseAdapter {
    public static final String TAG = "AdminQuestionAdapter";
    private Context context;
    private int layout;
    private ArrayList<Question> questionArrayList;
    private NotificationRepository notificationRepository;



    public AdminQuestionAdapter(Context context, int layout, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.layout = layout;
        this.questionArrayList = questionArrayList;
        this.notificationRepository = new NotificationRepository();
    }

    @Override
    public int getCount() {
        return questionArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        TextView tvQuestionTitle = (TextView) view.findViewById(R.id.tv_question_title);
        TextView tvQuestionContent = (TextView) view.findViewById(R.id.tv_question_content);
        TextView tvQuestionPosttime = (TextView) view.findViewById(R.id.tv_question_post_time);
        TextView tvQuestionAuthor = (TextView) view.findViewById(R.id.tv_author);
//        TextView tvUpvote = (TextView) view.findViewById(R.id.tv_vertical_vote_view_upvote);
//        TextView tvDownvote = (TextView) view.findViewById(R.id.tv_vertical_vote_view_downvote);

        TextView tvQuestionReport = (TextView) view.findViewById(R.id.tv_report);

        Chip chipSubject = (Chip) view.findViewById(R.id.chip_subject);
        Chip chipGrade = (Chip) view.findViewById(R.id.chip_grade);
        ImageButton ibBlock = (ImageButton) view.findViewById(R.id.ib_block);
        Question question = questionArrayList.get(i);
        tvQuestionTitle.setText(question.getTitle());
        tvQuestionContent.setText(question.getContent());
        tvQuestionPosttime.setText(notificationRepository.convertTimestampToRemaining(new Timestamp(question.getPostTime())));
//        tvQuestionPosttime.setText(question.getPostTime().toString());
//        tvUpvote.setText(String.valueOf(question.getUpvote()));
//        tvDownvote.setText(String.valueOf(question.getDownvote()));
        tvQuestionAuthor.setText(question.getAuthor());
        tvQuestionReport.setText(String.valueOf(question.getReport()));
        chipSubject.setText(question.getSubject());
        chipGrade.setText(question.getGrade());
        ibBlock.setVisibility(View.VISIBLE);
        if (question.getBlocked()) {
            ibBlock.setColorFilter(ContextCompat.getColor(context, R.color.design_default_color_error));
        }

        ibBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AAA", String.valueOf(i) + questionArrayList.get(i).getTitle());
                confirmAndHandleBlockOrUnblockQuestion(i);

//                // Testing
//                Notification notification = new Notification();
//                notification.pushUpvoteNotification(context, questionArrayList.get(i));
            }
        });

        tvQuestionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = QuestionDetailActivity.newInstance(context, questionArrayList.get(i));
                context.startActivity(intent);
            }
        });

        tvQuestionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = QuestionDetailActivity.newInstance(context, questionArrayList.get(i));
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void confirmAndHandleBlockOrUnblockQuestion(int pos) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        // Handle Block
        QuestionRepository questionRepository = new QuestionRepository();
        if (!questionArrayList.get(pos).getBlocked()) {
            alertBuilder.setTitle("Confirm Block Question");
            alertBuilder.setMessage("Are you sure you want to block this question?");
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    questionArrayList.get(pos).setBlocked(true);
                    notifyDataSetChanged();
                    questionRepository.blockQuestion(questionArrayList.get(pos).getId(), true, new TaskListener.State<Void>() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AAA", e.toString());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            //TO DO
                        }
                    });
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertBuilder.show();
        }


        // Handle Unblock
        else {
            alertBuilder.setTitle("Confirm Unblock Question");
            alertBuilder.setMessage("Are you sure you want to unblock this question?");
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    questionArrayList.get(pos).setBlocked(false);
                    notifyDataSetChanged();
                    questionRepository.blockQuestion(questionArrayList.get(pos).getId(), false, new TaskListener.State<Void>() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AAA", e.toString());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            //TO DO
                        }
                    });
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertBuilder.show();
        }
    }
}

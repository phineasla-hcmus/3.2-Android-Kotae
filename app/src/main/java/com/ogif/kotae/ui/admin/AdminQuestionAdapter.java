package com.ogif.kotae.ui.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.ui.main.AdminQuestionFragment;

import java.util.ArrayList;


public class AdminQuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Question> questionArrayList;
    private FirebaseFirestore db;


    public AdminQuestionAdapter(Context context, int layout, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.layout = layout;
        this.questionArrayList = questionArrayList;
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
        TextView tvQuestionUpvote = (TextView) view.findViewById(R.id.tv_up);
        TextView tvQuestionDownvote = (TextView) view.findViewById(R.id.tv_down);
        TextView tvQuestionReport = (TextView) view.findViewById(R.id.tv_report);
        Chip chipSubject = (Chip) view.findViewById(R.id.chip_subject);
        Chip chipGrade = (Chip) view.findViewById(R.id.chip_grade);
        ImageButton ibBlock = (ImageButton) view.findViewById(R.id.ib_block);

        Question question = questionArrayList.get(i);
        tvQuestionTitle.setText(question.getTitle());
        tvQuestionContent.setText(question.getContent());
//        tvQuestionPosttime.setText(question.getPostTime().toString());
        tvQuestionAuthor.setText(question.getAuthor());
        tvQuestionUpvote.setText(String.valueOf(question.getUpvote()));
        tvQuestionDownvote.setText(String.valueOf(question.getDownvote()));
        tvQuestionReport.setText(String.valueOf(question.getReport()));
        chipSubject.setText(question.getSubject());
        chipGrade.setText(question.getGrade());
        ibBlock.setVisibility(View.VISIBLE);
        if (question.isBlocked()) {
            ibBlock.setColorFilter(ContextCompat.getColor(context, R.color.design_default_color_error));
        }

        ibBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AAA", String.valueOf(i) + questionArrayList.get(i).getTitle());
                confirmAndHandleBlockOrUnblockQuestion(i);
            }
        });

        return view;
    }

    public void confirmAndHandleBlockOrUnblockQuestion(int pos) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        // Handle Block
        if (!questionArrayList.get(pos).isBlocked()) {
            alertBuilder.setTitle("Confirm Block Question");
            alertBuilder.setMessage("Are you sure you want to block this question?");
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    questionArrayList.get(pos).setBlocked(true);
                    notifyDataSetChanged();
                    updateBlockFireStore(pos, true);
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
                    updateBlockFireStore(pos, false);
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

    private void updateBlockFireStore(int pos, boolean blocked) {
        db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("questions")
                .document(questionArrayList.get(pos).getId());
        ref.update(
                "blocked", blocked
        );
    }
}

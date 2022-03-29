package com.ogif.kotae.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.User;

import java.util.ArrayList;


public class AdminQuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Question> questionArrayList;


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
//        TextView tvQuestionUpvote = (TextView) view.findViewById(R.id.tv_up);
//        TextView tvQuestionDownvote= (TextView) view.findViewById(R.id.tv_down);
        TextView tvQuestionReport= (TextView) view.findViewById(R.id.tv_report);
        Chip chipSubject = (Chip) view.findViewById(R.id.chip_subject);
        Chip chipGrade = (Chip) view.findViewById(R.id.chip_grade);
        ImageButton ibBlock = (ImageButton) view.findViewById(R.id.ib_block);

        Question question = questionArrayList.get(i);
        tvQuestionTitle.setText(question.getTitle());
        tvQuestionContent.setText(question.getContent());
//        tvQuestionPosttime.setText(question.getPostTime().toString());
        tvQuestionAuthor.setText(question.getAuthor());
//        tvQuestionUpvote.setText(String.valueOf(question.getUpvote()));
//        tvQuestionDownvote.setText(String.valueOf(question.getDownvote()));
        tvQuestionReport.setText(String.valueOf(question.getReport()));
        chipSubject.setText(question.getSubject());
        chipGrade.setText(question.getGrade());
        ibBlock.setVisibility(View.VISIBLE);
        if(question.isBlocked()){
           ibBlock.setColorFilter(R.color.design_default_color_error);
        }

        return view;
    }
}

package com.ogif.kotae.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;

public class ManagedQuestionAdapter extends ArrayAdapter<Post> {
    Activity context;
    int resource;

    public ManagedQuestionAdapter(@NonNull Activity context, int resource) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();

        View customView = inflater.inflate(this.resource, null);

        TextView title = (TextView) customView.findViewById(R.id.tv_question_title_admin);
        TextView content = (TextView) customView.findViewById(R.id.tv_question_content_admin);
        TextView posttime = (TextView) customView.findViewById(R.id.tv_question_posttime_admin);

        TextView author = (TextView) customView.findViewById(R.id.tv_author_admin);
        TextView upvote = (TextView) customView.findViewById(R.id.tv_upvote_admin);
        TextView downvote = (TextView) customView.findViewById(R.id.tv_downvote_admin);
        TextView report = (TextView) customView.findViewById(R.id.tv_report_admin);

        TextView grade = (TextView) customView.findViewById(R.id.tv_grade_admin);
        TextView subject = (TextView) customView.findViewById(R.id.tv_subject_admin);

        ImageView ivReport = (ImageView) customView.findViewById(R.id.iv_report_question_admin);

        Question question = (Question) getItem(position);
        title.setText(question.getTitle());
        content.setText(question.getContent());
        posttime.setText(question.getPostTime().toString());
        author.setText(question.getAuthorId());
        upvote.setText(Integer.toString(question.getUpvote()));
        downvote.setText(Integer.toString(question.getDownvote()));

        grade.setText(question.getGradeId());
        subject.setText(question.getSubjectId());

        if (question.getReport() != 0) {
            report.setText(Integer.toString(question.getReport()));
        } else {
            ivReport.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
        }

        return customView;
    }
}

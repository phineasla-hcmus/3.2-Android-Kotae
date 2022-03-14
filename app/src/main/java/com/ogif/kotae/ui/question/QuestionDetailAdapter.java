package com.ogif.kotae.ui.question;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;

// https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-types

class QuestionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_QUESTION = 0;
    private static final int ITEM_TYPE_ANSWER = 1;

    private Question question;
    private Answer[] answers;

    public QuestionDetailAdapter() {
        this.question = null;
        this.answers = new Answer[0];
    }

    private static class QuestionDetailHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView content;

        public QuestionDetailHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_question_detail_title);
            content = itemView.findViewById(R.id.tv_question_detail_content);
        }
    }

    private static class AnswerHolder extends RecyclerView.ViewHolder {

        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM_TYPE_QUESTION:
                return new QuestionDetailHolder(inflater
                        .inflate(R.layout.item_question_detail, parent, false));
            case ITEM_TYPE_ANSWER:
                return new AnswerHolder(inflater
                        .inflate(R.layout.item_answer, parent, false));
        }
        throw new IllegalStateException("Missing item type in QuestionDetailAdapter#onCreateViewHolder");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_QUESTION: {
                if (question != null) {
                    QuestionDetailHolder holder = (QuestionDetailHolder) viewHolder;
                    // Bind question to holder
                    holder.title.setText(question.getTitle());
                    holder.content.setText(question.getContent());
                }
                break;
            }
            case ITEM_TYPE_ANSWER: {
                AnswerHolder holder = (AnswerHolder) viewHolder;
                // Bind answers to holder
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        // Answer + question
        return answers.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // QUESTION_DETAIL_ITEM always on top
        return position == 0 ? ITEM_TYPE_QUESTION : ITEM_TYPE_ANSWER;
    }

    public void updateQuestion(Question question) {
        this.question = question;
        notifyItemChanged(0);
    }
}
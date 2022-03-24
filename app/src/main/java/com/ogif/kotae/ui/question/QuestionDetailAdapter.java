package com.ogif.kotae.ui.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;
import java.util.List;

// https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-types

class QuestionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_QUESTION = 0;
    private static final int ITEM_TYPE_ANSWER = 1;

    private final Context context;
    private Question question;
    private final List<Answer> answers;

    public QuestionDetailAdapter(Context context) {
        this.context = context;
        this.question = null;
        this.answers = new ArrayList<>();
    }

    private abstract static class PostHolder extends RecyclerView.ViewHolder {
        protected final TextView content;
        protected final RecyclerView images;
        protected final AuthorView author;
        protected final PostActionGroupView actions;

        public PostHolder(@NonNull View itemView, @IdRes int content, @IdRes int images, @IdRes int author, @IdRes int actions) {
            super(itemView);
            this.content = itemView.findViewById(content);
            this.images = itemView.findViewById(images);
            this.author = itemView.findViewById(author);
            this.actions = itemView.findViewById(actions);
        }
    }

    private static class QuestionDetailHolder extends PostHolder {
        private final TextView title;
        private final Chip subject;
        private final Chip grade;

        public QuestionDetailHolder(@NonNull View itemView) {
            super(itemView,
                    R.id.tv_question_detail_content,
                    R.id.recycler_view_question_detail_images,
                    R.id.author_view_question_detail,
                    R.id.action_group_question_detail);
            title = itemView.findViewById(R.id.tv_question_detail_title);
            subject = itemView.findViewById(R.id.chip_question_detail_subject);
            grade = itemView.findViewById(R.id.chip_question_detail_grade);
        }
    }

    private static class AnswerHolder extends PostHolder {
        public AnswerHolder(@NonNull View itemView) {
            super(itemView,
                    R.id.tv_answer_content,
                    R.id.recycler_view_answer_images,
                    R.id.author_view_answer,
                    R.id.action_group_answer);
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
                if (question == null)
                    return;
                bindCommonView(viewHolder, question);
                bindQuestion((QuestionDetailHolder) viewHolder);
                break;
            }
            case ITEM_TYPE_ANSWER: {
                // Because question always at position 0
                Answer answer = answers.get(position - 1);
                bindCommonView(viewHolder, answer);
                bindAnswer((AnswerHolder) viewHolder, answer);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        // Answer + question
        return answers.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // ITEM_TYPE_QUESTION always on top
        return position == 0 ? ITEM_TYPE_QUESTION : ITEM_TYPE_ANSWER;
    }

    public void updateQuestion(@NonNull Question question) {
        this.question = question;
        notifyItemChanged(0);
    }

    public void updateAnswers(@NonNull List<Answer> answers) {
        this.answers.addAll(answers);
        notifyItemRangeInserted(1, answers.size());
        // notifyDataSetChanged();
    }

    /**
     * Bind common Question and Answer views.
     */
    public void bindCommonView(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull Post post) {
        PostHolder holder = (PostHolder) viewHolder;
        RecyclerView.LayoutManager imagesLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.content.setText(post.getContent());
        holder.images.setLayoutManager(imagesLayoutManager);
        holder.author.setAuthorName(post.getAuthor());
        // TODO aw s, now we need to either fetch the User or store reputation in Post :facepalm:
        // holder.author.setReputation(post);
        // TODO bind author avatar
    }

    public void bindQuestion(@NonNull QuestionDetailHolder holder) {
        holder.title.setText(question.getTitle());
        holder.subject.setText(question.getSubject());
        holder.grade.setText(question.getGrade());
    }

    public void bindAnswer(@NonNull AnswerHolder holder, Answer answer) {

    }
}
package com.ogif.kotae.ui.questiondetail.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.comment.CommentFragment;
import com.ogif.kotae.ui.common.adapter.RecordAdapter;
import com.ogif.kotae.ui.common.view.VoteView;
import com.ogif.kotae.ui.main.ImageAdapter;
import com.ogif.kotae.ui.questiondetail.ClickedImageActivity;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;
import com.ogif.kotae.ui.questiondetail.view.AuthorView;
import com.ogif.kotae.utils.model.PostUtils;
import com.ogif.kotae.utils.text.MarkdownUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 * @implSpec <ul>
 * <li>Question will always at position 0</li>
 * </ul>
 * @see <a href="https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-types">
 * Multiple ViewHolder
 * </a>
 */
public class QuestionDetailAdapter extends RecordAdapter<Post> {
    private static final int ITEM_TYPE_QUESTION = 0;
    private static final int ITEM_TYPE_ANSWER = 1;

    public QuestionDetailAdapter(Context context) {
        super(context);
    }

    private abstract static class PostHolder extends RecyclerView.ViewHolder {
        protected final TextView content;
        protected final GridView images;
        protected final AuthorView author;
        protected final VoteView vote;
        protected final MaterialButton comment;

        public PostHolder(@NonNull View itemView, @IdRes int content, @IdRes int images, @IdRes int author, @IdRes int vote, @IdRes int comment) {
            super(itemView);
            this.content = itemView.findViewById(content);
            this.images = itemView.findViewById(images);
            this.author = itemView.findViewById(author);
            this.vote = itemView.findViewById(vote);
            this.comment = itemView.findViewById(comment);
        }
    }

    private static class QuestionDetailHolder extends PostHolder {
        private final TextView title;
        private final Chip subject;
        private final Chip grade;

        public QuestionDetailHolder(@NonNull View itemView) {
            super(itemView,
                    R.id.tv_question_detail_content,
                    R.id.gv_question_detail_image,
                    R.id.author_view_question_detail,
                    R.id.vote_view_question_detail,
                    R.id.btn_question_detail_comment);
            this.title = itemView.findViewById(R.id.tv_question_detail_title);
            this.subject = itemView.findViewById(R.id.chip_question_detail_subject);
            this.grade = itemView.findViewById(R.id.chip_question_detail_grade);
        }
    }

    private static class AnswerHolder extends PostHolder {
        private final MaterialButton more;

        public AnswerHolder(@NonNull View itemView) {
            super(itemView,
                    R.id.tv_answer_content,
                    R.id.img_answer,
                    R.id.author_view_answer,
                    R.id.vote_view_answer,
                    R.id.btn_answer_comment);

            this.more = itemView.findViewById(R.id.btn_answer_more);
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
                // Because question always at position 0
                Question question = (Question) items.get(0);
                bindCommonView(viewHolder, question, 0);
                bindQuestion((QuestionDetailHolder) viewHolder, question);
                break;
            }
            case ITEM_TYPE_ANSWER: {
                // Because question always at position 0
                Answer answer = (Answer) items.get(position);
                bindCommonView(viewHolder, answer, position);
                bindAnswer((AnswerHolder) viewHolder, answer);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        // ITEM_TYPE_QUESTION always on top
        return position == 0 ? ITEM_TYPE_QUESTION : ITEM_TYPE_ANSWER;
    }

    @Override
    public void setItems(@NonNull List<Post> items) {
        setItems(items, new PostUtils.ListComparator(items, this.items));
    }

    public void setQuestion(@NonNull Question question) {
        setItem(0, question);
    }

    public void addAnswer(@NonNull Answer answer) {
        addItem(answer);
    }

    public void addAnswers(@NonNull List<Answer> answers) {
        addItems(answers);
    }

    /**
     * Bind common Question and Answer views.
     */
    public void bindCommonView(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull Post post, int position) {
        PostHolder holder = (PostHolder) viewHolder;

        MarkdownUtils.setMarkdown(context, post.getContent(), holder.content);

        holder.author.setName(post.getAuthor());
        holder.author.setReputation(post.getXp());
        holder.vote.setHolder(post);
        holder.vote.setVoteState(post.getUpvote(), post.getDownvote(), post.getVoteState());
        holder.vote.setOnStateChangeListener((view, previous, current) -> {
            onVoteChangeListenerIfNotNull(position, view, previous, current);
        });
        holder.comment.setText(String.format(Locale.getDefault(), "%d", post.getComment()));
        holder.comment.setOnClickListener(v -> {
            CommentFragment bottomSheetDialogCommentFragment = CommentFragment.newInstance(post);
            showBottomSheetDialogFragment(bottomSheetDialogCommentFragment);
        });
        // TODO bind author avatar



        if (!post.getImageIds().isEmpty())
            Log.i("TAG", post.getImageIds().toString());
        else
            Log.i("TAG", "EMPTY");
        GridViewAdapter adapter = new GridViewAdapter(context, post.getImageIds());
        holder.images.setAdapter(adapter);
        holder.images.setVerticalSpacing(holder.images.getHorizontalSpacing());
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.images
                .getLayoutParams();
        mlp.setMargins(0, holder.images.getHorizontalSpacing(), 0, 0);

        holder.images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                context.startActivity(new Intent(context,ClickedImageActivity.class).putExtra("image",post.getImageId(position)));
            }
        });
    }

    public void bindQuestion(@NonNull QuestionDetailHolder holder, @NonNull Question question) {
        holder.title.setText(question.getTitle());
        holder.subject.setText(question.getSubject());
        holder.grade.setText(question.getGrade());

    }

    public void bindAnswer(@NonNull AnswerHolder holder, @NonNull Answer answer) {
        PopupMenu popup = new PopupMenu(context, holder.more);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_report) {
                showReportDialog(answer);
                return true;
            }
            return false;
        });
        holder.more.setOnClickListener(view -> {
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_answer, popup.getMenu());
            popup.show();
        });
    }

    private void showBottomSheetDialogFragment(@NonNull CommentFragment bottomSheet) {
        if (!bottomSheet.isAdded()) {
            QuestionDetailActivity activity = (QuestionDetailActivity) context;
            bottomSheet.show(activity.getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    public void showReportDialog(Post post) {

    }
}
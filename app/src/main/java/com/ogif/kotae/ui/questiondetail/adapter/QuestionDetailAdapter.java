package com.ogif.kotae.ui.questiondetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Vote;
import com.ogif.kotae.ui.VoteView;
import com.ogif.kotae.ui.comment.CommentFragment;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;
import com.ogif.kotae.ui.questiondetail.view.AuthorView;
import com.ogif.kotae.utils.text.MarkdownUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * @implSpec <ul>
 * <li>Question will always at position 0</li>
 * <li>{@link Vote#NONE is represented by null value}</li>
 * </ul>
 * @see <a href="https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-types">
 * Multiple ViewHolder
 * </a>
 */
public class QuestionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_QUESTION = 0;
    private static final int ITEM_TYPE_ANSWER = 1;

    private final Context context;
    private Question question;
    private Vote questionVote;
    private final List<Answer> answers;
    private final List<Vote> answerVotes;
    private VoteView.OnStateChangeListener voteChangeListener;

    public QuestionDetailAdapter(Context context) {
        this.context = context;
        this.question = null;
        this.answers = new ArrayList<>();
        this.answerVotes = new ArrayList<>();
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
                if (question == null)
                    return;
                bindCommonView(viewHolder, question, questionVote);
                bindQuestion((QuestionDetailHolder) viewHolder);
                break;
            }
            case ITEM_TYPE_ANSWER: {
                // Because question always at position 0
                Answer answer = answers.get(position - 1);
                // answerVotes might not done fetching yet
                Vote v = answerVotes.isEmpty() ? null : answerVotes.get(position - 1);
                bindCommonView(viewHolder, answer, v);
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

    /**
     * @implNote will be stored as null if current user hasn't voted the question
     */
    public void updateQuestionVote(@Nullable Vote vote) {
        this.questionVote = vote;
        notifyItemChanged(0);
    }

    public void addAnswers(@NonNull List<Answer> answers) {
        this.answers.addAll(answers);
        notifyItemRangeInserted(1, answers.size());
    }

    /**
     * @implNote any answer that user hasn't voted (aka answerVotes.containKey(answerId) == false)
     * will be stored as null
     */
    public void addAnswerVotes(@NonNull Map<String, Vote> answerVotes) {
        List<Vote> votes = new ArrayList<>(answers.size());
        for (Answer answer : answers) {
            votes.add(answerVotes.getOrDefault(answer.getId(), null));
        }
        this.answerVotes.addAll(votes);
        notifyItemRangeInserted(1, answerVotes.size());
    }

    public void clear() {
        question = null;
        questionVote = null;
        answers.clear();
        answerVotes.clear();
    }

    public void clearAndNotify() {
        int n = getItemCount();
        clear();
        notifyItemRangeRemoved(0, n);
    }

    /**
     * Bind common Question and Answer views.
     */
    public void bindCommonView(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull Post post, @Nullable Vote vote) {
        PostHolder holder = (PostHolder) viewHolder;
        MarkdownUtils.setMarkdown(context, post.getContent(), holder.content);
        // RecyclerView.LayoutManager imagesLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        // holder.images.setLayoutManager(imagesLayoutManager);
        holder.author.setName(post.getAuthor());
        holder.author.setReputation(post.getXp());
        int voteState = vote == null ? Vote.NONE : vote.isUpvote() ? Vote.UPVOTE : Vote.DOWNVOTE;
        holder.vote.setVoteState(post.getUpvote(), post.getDownvote(), voteState);
        holder.vote.setHolder(post);
        holder.vote.setOnStateChangeListener(voteChangeListener);
        holder.comment.setText(String.format(Locale.getDefault(), "%d", post.getComment()));
        holder.comment.setOnClickListener(v -> {
            CommentFragment bottomSheetDialogCommentFragment = CommentFragment.newInstance(post.getId());
            showBottomSheetDialogFragment(bottomSheetDialogCommentFragment);
        });
        // TODO bind author avatar
    }

    public void bindQuestion(@NonNull QuestionDetailHolder holder) {
        holder.title.setText(question.getTitle());
        holder.subject.setText(question.getSubject());
        holder.grade.setText(question.getGrade());
    }

    public void bindAnswer(@NonNull AnswerHolder holder, Answer answer) {
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

    private void showBottomSheetDialogFragment(CommentFragment bottomSheet) {
        if (!bottomSheet.isAdded()) {
            QuestionDetailActivity activity = (QuestionDetailActivity) context;
            bottomSheet.show(activity.getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    public void showReportDialog(Post post) {

    }

    public VoteView.OnStateChangeListener getOnVoteChangeListener() {
        return voteChangeListener;
    }

    public void setOnVoteChangeListener(@Nullable VoteView.OnStateChangeListener listener) {
        this.voteChangeListener = listener;
    }
}
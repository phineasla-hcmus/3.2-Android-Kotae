package com.ogif.kotae.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.listeners.ItemClickListener;
import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.markers.ItemResources;
import com.arthurivanets.adapster.model.BaseItem;
import com.google.android.material.chip.Chip;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.QuestionViewModel;
import com.ogif.kotae.ui.common.view.VerticalVoteView;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;
import com.ogif.kotae.ui.search.adapter.SearchAdapter;
import com.ogif.kotae.utils.DateUtils;

import org.jetbrains.annotations.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("rawtypes")
public final class SearchItem extends BaseItem {
    private Context context;

    @NonNull
    public SearchItem.ViewHolder init(@Nullable Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_question, parent, false);
        context = parent.getContext();

        return new SearchItem.ViewHolder(view);
    }

    @NonNull
    public androidx.recyclerview.widget.RecyclerView.ViewHolder init(Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater, ItemResources var4) {
        return (androidx.recyclerview.widget.RecyclerView.ViewHolder) this.init(adapter, parent, inflater);
    }

    @Override
    public void bind(@Nullable Adapter adapter, @NonNull BaseItem.ViewHolder viewHolder, @Nullable ItemResources resources) {
        super.bind(adapter, viewHolder, resources);
        Question model = (Question) this.getItemModel();

        ViewHolder holder = (ViewHolder) viewHolder;
        //holder.content.setText(model.getContent());
        holder.report.setVisibility(View.INVISIBLE);
        holder.reportCounter.setVisibility(View.INVISIBLE);
        holder.setMarkdown(model.getContent());
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
        this.setOnItemClickListener((ViewHolder) viewHolder, (view, item, position) -> {
            Intent intent = QuestionDetailActivity.newInstance(view.getContext(), model);
            view.getContext().startActivity(intent);
        });
    }

    public final void setOnItemClickListener(@NonNull SearchItem.ViewHolder viewHolder, @Nullable OnItemClickListener onItemClickListener) {
        viewHolder.layout.setOnClickListener((OnClickListener) (new ItemClickListener(this, 0, onItemClickListener)));
    }

    public int getLayout() {
        return R.layout.item_question;
    }

    public SearchItem(@NonNull Question itemModel) {
        super(itemModel);
    }

    public static final class ViewHolder extends com.arthurivanets.adapster.model.BaseItem.ViewHolder {
        TextView title, content, author, postTime, upvoteCounter, downvoteCounter, reportCounter;
        ImageButton report;
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

        public void setMarkdown(String content) {
            SearchAdapter.setMarkdown(content, this.content);
        }

    }

}

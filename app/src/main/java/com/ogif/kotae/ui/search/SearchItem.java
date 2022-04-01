package com.ogif.kotae.ui.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.listeners.ItemClickListener;
import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.markers.ItemResources;
import com.arthurivanets.adapster.model.BaseItem;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("rawtypes")
public final class SearchItem extends BaseItem {
    private Question question;

    @NonNull
    public SearchItem.ViewHolder init(@Nullable Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_search, parent, false);

        return new SearchItem.ViewHolder(view);
    }

    @NonNull
    public androidx.recyclerview.widget.RecyclerView.ViewHolder init(Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater, ItemResources var4) {
        return (androidx.recyclerview.widget.RecyclerView.ViewHolder) this.init(adapter, parent, inflater);
    }

    @Override
    public void bind(@Nullable Adapter adapter, @NonNull BaseItem.ViewHolder viewHolder, @Nullable ItemResources resources) {
        super.bind(adapter, viewHolder, resources);
        question = (Question) this.getItemModel();
        this.bindTitle((ViewHolder) viewHolder, question);
        this.bindProfileImage((ViewHolder) viewHolder);
        this.bindUsername((ViewHolder) viewHolder, question);
        this.bindPoint((ViewHolder) viewHolder, question);
        this.setOnItemClickListener((ViewHolder) viewHolder, (view, item, position) -> {
            Intent intent = QuestionDetailActivity.newInstance(view.getContext(), question);
            view.getContext().startActivity(intent);
        });
    }

    private void bindProfileImage(SearchItem.ViewHolder viewHolder) {
        viewHolder.getIvAvatar().setImageResource(R.drawable.ic_placeholder_user);
        viewHolder.getIvComment().setImageResource(R.drawable.ic_baseline_comment);
    }

    private void bindUsername(SearchItem.ViewHolder viewHolder, Question Question) {
        TextView tvUsername = viewHolder.getTvUsername();
        tvUsername.setText((CharSequence) Question.getAuthor());
    }

    private void bindPoint(SearchItem.ViewHolder viewHolder, Question Question) {
        TextView tvPoint = viewHolder.getTvPoint();
        //TODO: get point of current user
        tvPoint.setText(String.valueOf(20));
    }

    private void bindTitle(SearchItem.ViewHolder viewHolder, Question Question) {
        TextView tvTitle = viewHolder.getTvTitle();
        tvTitle.setText((CharSequence) Question.getTitle());
    }

    public final void setOnItemClickListener(@NonNull SearchItem.ViewHolder viewHolder, @Nullable OnItemClickListener onItemClickListener) {
        viewHolder.getRlContentContainer().setOnClickListener((OnClickListener) (new ItemClickListener(this, 0, onItemClickListener)));
    }

    public int getLayout() {
        return R.layout.item_search;
    }

    public SearchItem(@NonNull Question itemModel) {
        super(itemModel);
    }

    public static final class ViewHolder extends com.arthurivanets.adapster.model.BaseItem.ViewHolder {
        private final TextView tvUsername, tvTitle, tvPoint;
        private final ImageView ivAvatar, ivComment;
        private final RelativeLayout rlContentContainer;

        public final TextView getTvUsername() {
            return this.tvUsername;
        }

        public final TextView getTvPoint() {
            return this.tvPoint;
        }

        public final ImageView getIvAvatar() {
            return this.ivAvatar;
        }

        public final ImageView getIvComment() {
            return this.ivComment;
        }

        public final RelativeLayout getRlContentContainer() {
            return this.rlContentContainer;
        }

        public final TextView getTvTitle() {
            return this.tvTitle;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_search_item_title);
            this.tvUsername = (TextView) itemView.findViewById(R.id.tv_leaderboard_username);
            this.tvPoint = (TextView) itemView.findViewById(R.id.tv_leaderboard_point);
            this.ivAvatar = (ImageView) itemView.findViewById(R.id.civ_leaderboard_avatar);
            this.rlContentContainer = (RelativeLayout) itemView.findViewById(R.id.rl_content_container);
            this.ivComment = (ImageView) itemView.findViewById(R.id.iv_comment);
        }


    }

}

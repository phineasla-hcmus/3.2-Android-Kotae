package com.ogif.kotae.adapters.model;

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
import com.ogif.kotae.ui.question.QuestionDetailActivity;

import org.jetbrains.annotations.Nullable;

public final class SearchItem extends BaseItem {
    private Question question;

    @NonNull
    public SearchItem.ViewHolder init(@Nullable Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater) {
        View var10002 = inflater.inflate(R.layout.item_search, parent, false);

        return new SearchItem.ViewHolder(var10002);
    }

    public androidx.recyclerview.widget.RecyclerView.ViewHolder init(Adapter var1, ViewGroup var2, LayoutInflater var3, ItemResources var4) {
        return (androidx.recyclerview.widget.RecyclerView.ViewHolder) this.init(var1, var2, var3);
    }

    @Override
    public void bind(@Nullable Adapter adapter, @NonNull BaseItem.ViewHolder viewHolder, @Nullable ItemResources resources) {
        super.bind(adapter, viewHolder, resources);
        question = (Question) this.getItemModel();
        this.bindTitle((ViewHolder) viewHolder, question);
        this.bindProfileImage((ViewHolder) viewHolder, question);
        this.bindUsername((ViewHolder) viewHolder, question);
        this.bindFullName((ViewHolder) viewHolder, question);
        this.setOnItemClickListener((ViewHolder) viewHolder, new OnItemClickListener() {
            @Override
            public void onItemClicked(View view, Object item, int position) {
                Intent intent = QuestionDetailActivity.newInstance(view.getContext(), question);
                view.getContext().startActivity(intent);
            }
        });
    }

    private final void bindProfileImage(SearchItem.ViewHolder $this$bindProfileImage, Question Question) {
        $this$bindProfileImage.getProfileImageIv().setImageResource(R.drawable.ic_teacher);
        $this$bindProfileImage.getCommentIv().setImageResource(R.drawable.ic_baseline_comment);
    }

    private final void bindUsername(SearchItem.ViewHolder $this$bindUsername, Question Question) {
        TextView var10000 = $this$bindUsername.getUsernameTv();
        var10000.setText((CharSequence) Question.getAuthor());
    }

    private final void bindFullName(SearchItem.ViewHolder $this$bindFullName, Question Question) {
        TextView var10000 = $this$bindFullName.getFullNameTv();
        //TODO: get point of current user
        var10000.setText(String.valueOf(20));
    }

    private final void bindTitle(SearchItem.ViewHolder $this$bindTitle, Question Question) {
        TextView var10000 = $this$bindTitle.getTitle();
        var10000.setText((CharSequence) Question.getTitle());
    }

    public final void setOnItemClickListener(@NonNull SearchItem.ViewHolder viewHolder, @Nullable OnItemClickListener onItemClickListener) {
        viewHolder.getContentContainerRl().setOnClickListener((OnClickListener) (new ItemClickListener(this, 0, onItemClickListener)));
    }

    public int getLayout() {
        return R.layout.item_search;
    }

    public SearchItem(@NonNull Question itemModel) {
        super(itemModel);
    }

    public static final class ViewHolder extends com.arthurivanets.adapster.model.BaseItem.ViewHolder {
        private final TextView usernameTv, tvTitle;
        private final TextView fullNameTv;
        private final ImageView profileImageIv, commentIv;
        private final RelativeLayout contentContainerRl;

        public final TextView getUsernameTv() {
            return this.usernameTv;
        }

        public final TextView getFullNameTv() {
            return this.fullNameTv;
        }

        public final ImageView getProfileImageIv() {
            return this.profileImageIv;
        }

        public final ImageView getCommentIv() {
            return this.commentIv;
        }

        public final RelativeLayout getContentContainerRl() {
            return this.contentContainerRl;
        }

        public final TextView getTitle() {
            return this.tvTitle;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_search_item_title);
            this.usernameTv = (TextView) itemView.findViewById(R.id.tv_item_username);
            this.fullNameTv = (TextView) itemView.findViewById(R.id.tv_item_point);
            this.profileImageIv = (ImageView) itemView.findViewById(R.id.civ_item_avatar);
            this.contentContainerRl = (RelativeLayout) itemView.findViewById(R.id.contentContainerRl);
            this.commentIv = (ImageView) itemView.findViewById(R.id.firstButtonIv);
        }


    }

}

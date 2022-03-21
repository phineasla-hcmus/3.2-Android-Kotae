package com.ogif.kotae.adapters.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.listeners.ItemClickListener;
import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.markers.ItemResources;
import com.arthurivanets.adapster.model.BaseItem;
import com.ogif.kotae.data.model.Question;

import org.jetbrains.annotations.Nullable;

import com.ogif.kotae.R;

public final class SearchItem extends BaseItem {
    @NonNull
    public SearchItem.ViewHolder init(@Nullable Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater) {
        View var10002 = inflater.inflate(R.layout.item_search, parent, false);
        return new SearchItem.ViewHolder(var10002);
    }

    public androidx.recyclerview.widget.RecyclerView.ViewHolder init(Adapter var1, ViewGroup var2, LayoutInflater var3, ItemResources var4) {
        return (androidx.recyclerview.widget.RecyclerView.ViewHolder) this.init(var1, var2, var3);
    }

    public void bind(@Nullable Adapter adapter, @NonNull SearchItem.ViewHolder viewHolder) {
        super.bind(adapter, (com.arthurivanets.adapster.model.BaseItem.ViewHolder) viewHolder, null);
        Question var10002 = (Question) this.getItemModel();
        this.bindProfileImage(viewHolder, var10002);
        var10002 = (Question) this.getItemModel();
        this.bindUsername(viewHolder, var10002);
        var10002 = (Question) this.getItemModel();
        this.bindFullName(viewHolder, var10002);
    }

    public void bind(Adapter var1, androidx.recyclerview.widget.RecyclerView.ViewHolder var2, ItemResources var3) {
        this.bind(var1, (SearchItem.ViewHolder) var2, null);
    }

    public void bind(Adapter var1, com.arthurivanets.adapster.model.BaseItem.ViewHolder var2, ItemResources var3) {
        super.bind(var1, var2, var3);
        this.bind(var1, (ViewHolder) var2, null);
    }

    private final void bindProfileImage(SearchItem.ViewHolder $this$bindProfileImage, Question Question) {
        $this$bindProfileImage.getProfileImageIv().setImageResource(Question.getDownvote());
    }

    private final void bindUsername(SearchItem.ViewHolder $this$bindUsername, Question Question) {
        TextView var10000 = $this$bindUsername.getUsernameTv();
        var10000.setText((CharSequence) Question.getTitle());
    }

    private final void bindFullName(SearchItem.ViewHolder $this$bindFullName, Question Question) {
        TextView var10000 = $this$bindFullName.getFullNameTv();
        var10000.setText((CharSequence) Question.getAuthor());
    }

    public final void setOnItemClickListener(@NonNull SearchItem.ViewHolder viewHolder, @Nullable OnItemClickListener onItemClickListener) {
        viewHolder.getContentContainerRl().setOnClickListener((OnClickListener) (new ItemClickListener(this, 0, onItemClickListener)));
    }

    public int getLayout() {
        return 1300012;
    }

    public SearchItem(@NonNull Question itemModel) {
        super(itemModel);
    }

    public static final class ViewHolder extends com.arthurivanets.adapster.model.BaseItem.ViewHolder {
        private final TextView usernameTv;
        private final TextView fullNameTv;
        private final ImageView profileImageIv;
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

        public final RelativeLayout getContentContainerRl() {
            return this.contentContainerRl;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usernameTv = (TextView) itemView.findViewById(R.id.usernameTv);
            this.fullNameTv = (TextView) itemView.findViewById(R.id.fullNameTv);
            this.profileImageIv = (ImageView) itemView.findViewById(R.id.profileImageIv);
            this.contentContainerRl = (RelativeLayout) itemView.findViewById(R.id.contentContainerRl);
        }
    }

}

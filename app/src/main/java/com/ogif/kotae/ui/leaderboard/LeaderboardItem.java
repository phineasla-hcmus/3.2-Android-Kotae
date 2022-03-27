package com.ogif.kotae.ui.leaderboard;

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
import com.ogif.kotae.data.model.User;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("rawtypes")
public final class LeaderboardItem extends BaseItem {

    @NonNull
    public LeaderboardItem.ViewHolder init(@Nullable Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_leaderboard, parent, false);

        return new LeaderboardItem.ViewHolder(view);
    }

    @NonNull
    public androidx.recyclerview.widget.RecyclerView.ViewHolder init(Adapter adapter, @NonNull ViewGroup parent, @NonNull LayoutInflater inflater, ItemResources var4) {
        return (androidx.recyclerview.widget.RecyclerView.ViewHolder) this.init(adapter, parent, inflater);
    }

    @Override
    public void bind(@Nullable Adapter adapter, @NonNull BaseItem.ViewHolder viewHolder, @Nullable ItemResources resources) {
        super.bind(adapter, viewHolder, resources);
        User user = (User) this.getItemModel();
        this.bindProfileImage((LeaderboardItem.ViewHolder) viewHolder, user);
        this.bindUsername((LeaderboardItem.ViewHolder) viewHolder, user);
        this.bindPoint((LeaderboardItem.ViewHolder) viewHolder, user);
//        this.setOnItemClickListener((LeaderboardItem.ViewHolder) viewHolder, new OnItemClickListener() {
//            @Override
//            public void onItemClicked(View view, Object item, int position) {
//                Intent intent = UserDetailActivity.newInstance(view.getContext(), user);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    private void bindProfileImage(LeaderboardItem.ViewHolder viewHolder, User User) {
        viewHolder.getIvAvatar().setImageResource(R.drawable.ic_teacher);
    }

    private void bindUsername(LeaderboardItem.ViewHolder viewHolder, User User) {
        TextView tvUsername = viewHolder.getTvUsername();
        tvUsername.setText((CharSequence) User.getUsername());
    }

    private void bindPoint(LeaderboardItem.ViewHolder viewHolder, User User) {
        TextView tvPoint = viewHolder.getTvPoint();
        //TODO: get point of current user
        tvPoint.setText(String.valueOf(20));
    }

    public final void setOnItemClickListener(@NonNull LeaderboardItem.ViewHolder viewHolder, @Nullable OnItemClickListener onItemClickListener) {
        viewHolder.getRlContentContainer().setOnClickListener((OnClickListener) (new ItemClickListener(this, 0, onItemClickListener)));
    }

    public int getLayout() {
        return R.layout.item_leaderboard;
    }

    public LeaderboardItem(@NonNull User itemModel) {
        super(itemModel);
    }

    public static final class ViewHolder extends com.arthurivanets.adapster.model.BaseItem.ViewHolder {
        private final TextView tvUsername, tvPoint;
        private final ImageView ivAvatar;
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

        public final RelativeLayout getRlContentContainer() {
            return this.rlContentContainer;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvUsername = (TextView) itemView.findViewById(R.id.tv_leaderboard_username);
            this.tvPoint = (TextView) itemView.findViewById(R.id.tv_leaderboard_point);
            this.ivAvatar = (ImageView) itemView.findViewById(R.id.civ_leaderboard_avatar);
            this.rlContentContainer = (RelativeLayout) itemView.findViewById(R.id.rl_content_container);
        }
    }

}


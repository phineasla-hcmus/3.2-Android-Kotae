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
import com.ogif.kotae.data.model.ManagedUser;

public class ManagedUserAdapter extends ArrayAdapter<ManagedUser> {
    private Activity context;
    private int resource;

    public ManagedUserAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();

        View customView = inflater.inflate(this.resource,null);

        TextView userName = (TextView) customView.findViewById(R.id.tv_user_name_admin);
        TextView downvote = (TextView) customView.findViewById(R.id.tv_user_downvote_admin);
        TextView upvote = (TextView) customView.findViewById(R.id.tv_user_upvote_admin);
        TextView answer = (TextView) customView.findViewById(R.id.tv_user_answer_admin);
        TextView report = (TextView) customView.findViewById(R.id.tv_user_report_admin);
        TextView totalDaysBlocked = (TextView) customView.findViewById(R.id.tv_user_total_days_blocked_admin);
        ImageView ivBlock = (ImageView) customView.findViewById(R.id.iv_block);

        ManagedUser managedUser = getItem(position);

        userName.setText(managedUser.getUserName());
        downvote.setText(Integer.toString(managedUser.getDownvote()));
        upvote.setText(Integer.toString(managedUser.getUpvote()));
        answer.setText(Integer.toString(managedUser.getAnswer()));
        report.setText(Integer.toString(managedUser.getReport()));

        if(managedUser.isBlocked() == false){
            ivBlock.setVisibility(View.GONE);
            totalDaysBlocked.setVisibility(View.GONE);
        }

        return customView;
    }
}

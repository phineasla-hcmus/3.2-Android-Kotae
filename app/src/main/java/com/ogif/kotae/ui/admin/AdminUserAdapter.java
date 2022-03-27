package com.ogif.kotae.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.User;

import java.util.ArrayList;

public class AdminUserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<User> userArrayList;

    public AdminUserAdapter(Context context, int layout, ArrayList<User> userArrayList) {
        this.context = context;
        this.layout = layout;
        this.userArrayList = userArrayList;
    }

    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);

        ImageView ivBlock = (ImageView) view.findViewById(R.id.iv_block);
//        ImageView ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
        TextView tvReport = (TextView) view.findViewById(R.id.tv_user_report);

        User user = userArrayList.get(i);
        tvUsername.setText(user.getUsername());
        tvReport.setText(String.valueOf(user.getReport()));

        if (user.isBlocked()) {
            ivBlock.setVisibility(View.VISIBLE);
        }

        // How to set avatar to imageview?
//        if (user.getAvatar().length() != 0) {
//            ivAvatar.setImageResource(user.getAvatar());
//        }

        return view;
    }
}

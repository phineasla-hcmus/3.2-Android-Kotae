package com.ogif.kotae.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ogif.kotae.ui.main.AdminQuestionFragment;
import com.ogif.kotae.ui.main.AdminUserFragment;

public class ViewPager2AdminAdapter extends FragmentStateAdapter {
    public ViewPager2AdminAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: {
                return new AdminUserFragment();
            }
            default: {
                return new AdminQuestionFragment();
            }
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

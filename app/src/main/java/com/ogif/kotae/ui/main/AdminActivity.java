package com.ogif.kotae.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ogif.kotae.R;
import com.ogif.kotae.utils.ui.ViewPager2AdminAdapter;


public class AdminActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPager2AdminAdapter viewPager2AdminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tabLayout = (TabLayout) findViewById(R.id.tabbar_admin);
        viewPager2 = (ViewPager2) findViewById(R.id.vpg2_admin);
        viewPager2AdminAdapter = new ViewPager2AdminAdapter(this);
        viewPager2.setAdapter(viewPager2AdminAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 1: {
                        tab.setText("User");
                        break;
                    }
                    default: {
                        tab.setText("Question");
                        break;
                    }
                }
            }
        }).attach();
    }
}
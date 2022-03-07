package com.ogif.kotae.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private ExtendedFloatingActionButton fabPostQuestion;
    private Toolbar toolbar;
    private TextView tvQuestionContent;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        tvQuestionContent = binding.tvQuestionContentDescription;

        tvQuestionContent.setOnClickListener(view -> {
            startQuestionContentActivity();
        });


        fabPostQuestion = (ExtendedFloatingActionButton) binding.fabPostQuestion;

        fabPostQuestion.setOnClickListener(v -> {

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startQuestionContentActivity() {
        Intent intent = new Intent(getApplicationContext(), QuestionContentActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();
    }
}
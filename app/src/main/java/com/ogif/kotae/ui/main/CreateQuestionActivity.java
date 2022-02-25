package com.ogif.kotae.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private ExtendedFloatingActionButton fabPostQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());

        fabPostQuestion = (ExtendedFloatingActionButton) binding.fabPostQuestion;

        fabPostQuestion.setOnClickListener(v -> {
            startQuestionContentActivity();
        });
    }

    private void startQuestionContentActivity() {
        Intent intent = new Intent(this, QuestionContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
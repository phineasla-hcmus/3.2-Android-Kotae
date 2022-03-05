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
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private ExtendedFloatingActionButton fabPostQuestion;
    private Toolbar toolbar;
    private TextView tvQuestionContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());

        tvQuestionContent = binding.tvQuestionContentDescription;

        tvQuestionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("abc", "Clicked");
                startQuestionContentActivity();
            }
        });

//        toolbar = (Toolbar) binding.toolbarCreateQuestion;
//        this.setSupportActionBar(toolbar);
//
//        // calling the action bar
//        ActionBar actionBar = getSupportActionBar();
////
////        // showing the back button in action bar
//        actionBar.setDisplayHomeAsUpEnabled(true);
////
//        actionBar.setTitle(R.string.question);


        fabPostQuestion = (ExtendedFloatingActionButton) binding.fabPostQuestion;

        fabPostQuestion.setOnClickListener(v -> {
            this.finish();
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
        Intent intent = new Intent(this, QuestionContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
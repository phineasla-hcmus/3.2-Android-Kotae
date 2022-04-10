package com.ogif.kotae.ui.createanswer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityCreateAnswerBinding;
import com.ogif.kotae.ui.questiondetail.AnswerViewModel;
import com.ogif.kotae.utils.text.MarkdownUtils;

import java.util.Objects;


public class CreateAnswerActivity extends AppCompatActivity {

    private ActivityCreateAnswerBinding binding;
    private ActivityResultLauncher<Intent> answerActivityResultLauncher;
    private String content;
    private AnswerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateAnswerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.viewModel = new ViewModelProvider(this).get(AnswerViewModel.class);
        this.setSupportActionBar(binding.tbCreateAnswer);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Create answer");

        binding.etContent.setOnClickListener(v -> startAnswerContentActivity());

        binding.fabPostAnswer.setOnClickListener(v -> {
            String content = Objects.requireNonNull(binding.etContent.getText()).toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, getResources().getString(R.string.missing_answer_content), Toast.LENGTH_SHORT).show();
                return;
            }
            binding.fabPostAnswer.setEnabled(false);
            String id = getIntent().getStringExtra("questionId");
            this.viewModel.createAnswer(id, content);
            this.finish();
        });


        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        answerActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        content = Objects.requireNonNull(data).getStringExtra(Intent.EXTRA_TEXT);
                        MarkdownUtils.setMarkdown(getApplicationContext(), content, binding.etContent);
                    }
                });
    }

    public void startAnswerContentActivity() {
        if (TextUtils.isEmpty(content)) {
            content = Objects.requireNonNull(binding.etContent.getText()).toString();
        }
        String description = getResources().getString(R.string.create_answer_content_description);
        Intent intent = new Intent(this, AnswerContentActivity.class);
        if (!content.equals(description)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }
        answerActivityResultLauncher.launch(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
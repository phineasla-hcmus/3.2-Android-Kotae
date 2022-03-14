package com.ogif.kotae.ui.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityCreateAnswerBinding;
import com.ogif.kotae.utils.model.MarkdownUtils;


public class CreateAnswerActivity extends AppCompatActivity {

    private ActivityCreateAnswerBinding binding;
    private ExtendedFloatingActionButton fabPostQuestion;
    private Toolbar toolbar;
    private EditText etContent;
    private View view;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateAnswerBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        toolbar = (Toolbar) binding.tbCreateQuestion;
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Question detail");

        etContent = binding.etContent;

        etContent.setOnClickListener(view -> {
            startAnswerContentActivity();
        });

        fabPostQuestion = (ExtendedFloatingActionButton) binding.fabPostQuestion;

//        fabPostQuestion.setOnClickListener(v -> {
//            if (isValid()) {
//                // TODO: Create answer on Firebase
//            } else {
//                Toast.makeText(this, "Please check your input again", Toast.LENGTH_SHORT).show();
//            }
//        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            content = data.getStringExtra(Intent.EXTRA_TEXT);
                            MarkdownUtils.setMarkdown(getApplicationContext(), content, etContent);
                        }
                    }
                });


    }

    public void startAnswerContentActivity() {
        if (TextUtils.isEmpty(content)) {
            content = etContent.getText().toString();
        }
        String description = getResources().getString(R.string.create_answer_content_description);
        Intent intent = new Intent(this, AnswerContentActivity.class);
        if (!content.equals(description)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }
        someActivityResultLauncher.launch(intent);
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
}
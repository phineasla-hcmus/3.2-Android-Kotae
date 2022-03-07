package com.ogif.kotae.ui.question;

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
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;

import org.commonmark.node.Node;

import io.noties.markwon.Markwon;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private ExtendedFloatingActionButton fabPostQuestion;
    private Toolbar toolbar;
    private EditText etContent;
    private View view;
    private AutoCompleteTextView atcvGrade, atcvSubject;
    private ArrayAdapter<String> gradeAdapter, subjectAdapter;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private String title, content, selectedGrade, selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        toolbar = (Toolbar) binding.tbCreateQuestion;
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Home");

        etContent = binding.etContent;

        etContent.setOnClickListener(view -> {
            startQuestionContentActivity();
        });

        fabPostQuestion = (ExtendedFloatingActionButton) binding.fabPostQuestion;

        fabPostQuestion.setOnClickListener(v -> {
            if (isValid()) {
                // TODO: Create question on Firebase
            } else {
                Toast.makeText(this, "Please check your input again", Toast.LENGTH_SHORT).show();
            }
        });

        atcvGrade = binding.actvQuestionCategoryGrade;
        atcvSubject = binding.atcvQuestionCategorySubject;

        String[] grades = new String[]{"Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5"};
        gradeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, grades);
        atcvGrade.setAdapter(gradeAdapter);
        atcvGrade.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedGrade = adapterView.getItemAtPosition(i).toString();
        });

        String[] subjects = new String[]{"Math", "Literature", "Biology", "Physics", "English", "History"};
        subjectAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, subjects);
        atcvSubject.setAdapter(subjectAdapter);
        atcvSubject.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedSubject = adapterView.getItemAtPosition(i).toString();
        });

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
                            setMarkdown();
                        }
                    }
                });


    }

    public void startQuestionContentActivity() {
        if (TextUtils.isEmpty(content)) {
            content = etContent.getText().toString();
        }
        String description = getResources().getString(R.string.create_question_content_description);
        Intent intent = new Intent(this, QuestionContentActivity.class);
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

    private void setMarkdown() {
        // obtain an instance of Markwon
        final Markwon markwon = Markwon.create(getApplicationContext());

        // parse markdown to commonmark-java Node
        final Node node = markwon.parse(content);

        // create styled text from parsed Node
        final Spanned markdown = markwon.render(node);

        // use it on a TextView
        markwon.setParsedMarkdown(etContent, markdown);
    }

    private boolean isValid() {
        title = binding.tvQuestionTitle.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(selectedGrade) || TextUtils.isEmpty(selectedSubject)) {
            return false;
        }
        return true;
    }
}
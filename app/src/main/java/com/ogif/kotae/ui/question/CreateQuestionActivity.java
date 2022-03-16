package com.ogif.kotae.ui.question;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Grade;
import com.ogif.kotae.data.model.Subject;
import com.ogif.kotae.data.repository.GradeRepository;
import com.ogif.kotae.data.repository.SubjectRepository;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;
import com.ogif.kotae.ui.QuestionViewModel;
import com.ogif.kotae.utils.GradeAdapter;
import com.ogif.kotae.utils.SubjectAdapter;
import com.ogif.kotae.utils.model.MarkdownUtils;
import com.ogif.kotae.utils.model.QuestionUtils;
import com.ogif.kotae.utils.text.TextValidator;

import java.util.List;
import java.util.Objects;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private Toolbar toolbar;
    private GradeAdapter gradeAdapter;
    private SubjectAdapter subjectAdapter;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private String title, content, selectedGrade, selectedSubject;
    private QuestionViewModel viewModel;
    private GradeRepository gradeRepository;
    private SubjectRepository subjectRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        this.viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        toolbar = (Toolbar) binding.tbCreateQuestion;
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create question");

        EditText[] ets = {binding.etCreateQuestionTitle,
                          binding.etContent,
                          binding.actvQuestionCategoryGrade,
                          binding.atcvQuestionCategorySubject};

        binding.etContent.setOnClickListener(view -> {
            startQuestionContentActivity();
        });

        gradeRepository = new GradeRepository();
        gradeRepository.getAll(new TaskListener.State<List<Grade>>() {
            @Override
            public void onSuccess(@NonNull List<Grade> result) {
                Grade[] grades = result.toArray(new Grade[result.size()]);
                gradeAdapter = new GradeAdapter(getApplicationContext(), R.layout.dropdown_item, grades);
                binding.actvQuestionCategoryGrade.setAdapter(gradeAdapter);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO
            }
        });

        binding.actvQuestionCategoryGrade.setOnItemClickListener((adapterView, view, i, l) -> {
            Grade grade = (Grade) adapterView.getItemAtPosition(i);
            selectedGrade = grade.getId();
        });

        subjectRepository = new SubjectRepository();
        subjectRepository.getAllSubjects(subjects -> {
            subjectAdapter = new SubjectAdapter(getApplicationContext(), R.layout.dropdown_item, subjects
                    .toArray(new Subject[subjects.size()]));
            binding.atcvQuestionCategorySubject.setAdapter(subjectAdapter);
        });

        binding.atcvQuestionCategorySubject.setOnItemClickListener((adapterView, view, i, l) -> {
            Subject subject = (Subject) adapterView.getItemAtPosition(i);
            selectedSubject = subject.getId();
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
                            MarkdownUtils.setMarkdown(getApplicationContext(), content, binding.etContent);
                        }
                    }
                });

        binding.etCreateQuestionTitle.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable title) {
                TextInputLayout til = binding.tilCreateQuestionTitle;
                if (QuestionUtils.isTitleValid(title) == QuestionUtils.INVALID_TITLE_LENGTH) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_question_title_length));
                } else {
                    til.setErrorEnabled(false);
                }
            }
        });

        binding.fabPostQuestion.setOnClickListener(v -> {
            for (EditText et : ets) {
                if (TextUtils.isEmpty(et.getText())) {
                    Toast.makeText(this, getResources().getString(R.string.create_question_error_missing), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }
            String title = Objects.requireNonNull(binding.etCreateQuestionTitle.getText())
                    .toString();
            String content = Objects.requireNonNull(binding.etContent.getText()).toString();

            binding.fabPostQuestion.setEnabled(false);
            this.viewModel.createQuestion(title, content, selectedSubject, selectedGrade);
            this.finish();
        });
    }

    public void startQuestionContentActivity() {
        if (TextUtils.isEmpty(content)) {
            content = binding.etContent.getText().toString();
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
}
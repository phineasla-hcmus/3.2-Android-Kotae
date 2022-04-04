package com.ogif.kotae.ui.createquestion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Grade;
import com.ogif.kotae.data.model.Subject;
import com.ogif.kotae.data.repository.GradeRepository;
import com.ogif.kotae.data.repository.StorageRepository;
import com.ogif.kotae.data.repository.SubjectRepository;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;
import com.ogif.kotae.ui.LoadingDialog;
import com.ogif.kotae.ui.QuestionViewModel;
import com.ogif.kotae.ui.main.ImageAdapter;
import com.ogif.kotae.ui.main.adapter.GradeAdapter;
import com.ogif.kotae.ui.main.adapter.SubjectAdapter;
import com.ogif.kotae.utils.model.QuestionUtils;
import com.ogif.kotae.utils.text.MarkdownUtils;
import com.ogif.kotae.utils.text.TextValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateQuestionActivity extends AppCompatActivity {

    private ActivityCreateQuestionBinding binding;
    private GradeAdapter gradeAdapter;
    private SubjectAdapter subjectAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String content, selectedGradeId, selectedSubjectId, selectedGradeName = "", selectedSubjectName = "";
    private QuestionViewModel viewModel;

    private List<Subject> subjects = new ArrayList<>();
    private final int PICK_IMAGE_MULTIPLE = 1;
    private Uri imageUri;
    private ArrayList<Uri> imageList =  new ArrayList<>();
    private  FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private int uploadCount = 0;
    private ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        this.viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        this.setSupportActionBar(binding.tbCreateQuestion);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Create question");

        binding.etContent.setOnClickListener(view -> startQuestionContentActivity());

        GradeRepository gradeRepository = new GradeRepository();
        gradeRepository.getAll(new TaskListener.State<List<Grade>>() {
            @Override
            public void onSuccess(@NonNull List<Grade> result) {
                Grade[] grades = result.toArray(new Grade[0]);
                gradeAdapter = new GradeAdapter(getApplicationContext(), R.layout.dropdown_item, grades);
                binding.actvQuestionCategoryGrade.setAdapter(gradeAdapter);
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO
            }
        });

        SubjectRepository subjectRepository = new SubjectRepository();
        subjectRepository.getAllSubjects(subjects -> {
            subjectAdapter = new SubjectAdapter(getApplicationContext(), R.layout.dropdown_item, this.subjects.toArray(new Subject[0]));
            this.subjects = subjects;
            binding.atcvQuestionCategorySubject.setAdapter(subjectAdapter);
        });

        binding.actvQuestionCategoryGrade.setOnItemClickListener((adapterView, view, i, l) -> {
            Grade grade = (Grade) adapterView.getItemAtPosition(i);
            selectedGradeId = grade.getId();
            selectedGradeName = grade.getName();
            List<Subject> temp = new ArrayList<>(this.subjects);

            int level = Integer.valueOf(selectedGradeId.substring(1));

            if (level <= 7) {
                temp.removeIf(subject -> subject.getName().equals("Chemistry"));
            }

            if (level <= 2) {
                temp.removeIf(subject -> subject.getName().equals("Informatics"));
                temp.removeIf(subject -> subject.getName().equals("Technology"));
            }

            if (level <= 3) {
                temp.removeIf(subject -> subject.getName().equals("History"));
                temp.removeIf(subject -> subject.getName().equals("Geography"));
            }

            if (level <= 5)
            {
                temp.removeIf(subject -> subject.getName().equals("Physics"));
                temp.removeIf(subject -> subject.getName().equals("Biology"));
            }

            subjectAdapter.setItems(temp.toArray(new Subject[0]));
        });

        binding.atcvQuestionCategorySubject.setOnItemClickListener((adapterView, view, i, l) -> {
            Subject subject = (Subject) adapterView.getItemAtPosition(i);
            selectedSubjectId = subject.getId();
            selectedSubjectName = subject.getName();
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        content = Objects.requireNonNull(data).getStringExtra(Intent.EXTRA_TEXT);
                        MarkdownUtils.setMarkdown(getApplicationContext(), content, binding.etContent);
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
            String title = Objects.requireNonNull(binding.etCreateQuestionTitle.getText())
                    .toString();
            if (QuestionUtils.isTitleValid(title) == QuestionUtils.INVALID_TITLE_LENGTH) {
                Toast.makeText(this, getResources().getString(R.string.et_error_question_title_length), Toast.LENGTH_SHORT).show();
                return;
            }
            String content = Objects.requireNonNull(binding.etContent.getText()).toString();
            if (QuestionUtils.isContentValid(content) == QuestionUtils.INVALID_CONTENT_LENGTH) {
                Toast.makeText(this, getResources().getString(R.string.invalid_question_content_length), Toast.LENGTH_SHORT).show();
                return;
            }
            if (QuestionUtils.isGradeValid(selectedGradeName) == QuestionUtils.NOT_SELECTED_GRADE) {
                Toast.makeText(this, getResources().getString(R.string.missing_selected_grade), Toast.LENGTH_SHORT).show();
                return;
            }
            if (QuestionUtils.isSubjectValid(selectedSubjectName) == QuestionUtils.NOT_SELECTED_SUBJECT) {
                Toast.makeText(this, getResources().getString(R.string.missing_selected_subject), Toast.LENGTH_SHORT).show();
                return;
            }
            binding.fabPostQuestion.setEnabled(false);
            this.viewModel.createQuestion(title, content, selectedSubjectId, selectedGradeId, selectedSubjectName, selectedGradeName);

            if (!imageList.isEmpty()){ uploadImage();}
            this.finish();
        });

        binding.btnQuestionImages.setOnClickListener(v -> {
            selectImage();
        });
    }

    public void startQuestionContentActivity() {
        if (TextUtils.isEmpty(content)) {
            content = Objects.requireNonNull(binding.etContent.getText()).toString();
        }
        String description = getResources().getString(R.string.create_question_content_description);
        Intent intent = new Intent(this, QuestionContentActivity.class);
        if (!content.equals(description)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectImage(){
        if (imageList!= null)
            imageList.clear();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_MULTIPLE);

    }
    public void uploadImage(){
//        ProgressDialog progressDialog
//                = new ProgressDialog(CreateQuestionActivity.this);
//        progressDialog.setMessage("The process may take a little long");
//        progressDialog.show();
//        LoadingDialog loadingDialog = new LoadingDialog(CreateQuestionActivity.this);
//        loadingDialog.startLoadingDialog();
        StorageRepository storageRepository = new StorageRepository();
        storageRepository.uploadQuestionImages(imageList,uploadCount,
                CreateQuestionActivity.this,imageAdapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {

                if (data.getData() != null) {
                    binding.hsv.setVisibility(View.VISIBLE);
                    imageUri = data.getData();
                    imageList.add(imageUri);

                    imageAdapter = new ImageAdapter(getApplicationContext(),imageList);
                    binding.gvQuestionImage.setAdapter(imageAdapter);
                    binding.gvQuestionImage.setVerticalSpacing(binding.gvQuestionImage.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) binding.gvQuestionImage
                            .getLayoutParams();
                    mlp.setMargins(0, binding.gvQuestionImage.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        binding.hsv.setVisibility(View.VISIBLE);
                        int countClipData = data.getClipData().getItemCount();
                        int currentImageSelect = 0;
                        while (currentImageSelect < countClipData) {
                            imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                            imageList.add(imageUri);
                            currentImageSelect++;
                        }

                        imageAdapter = new ImageAdapter(getApplicationContext(),imageList);
                        binding.gvQuestionImage.setAdapter(imageAdapter);
                        binding.gvQuestionImage.setVerticalSpacing(binding.gvQuestionImage.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) binding.gvQuestionImage
                                .getLayoutParams();
                        mlp.setMargins(0, binding.gvQuestionImage.getHorizontalSpacing(), 0, 0);
                    }
                }
            }

        }catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
package com.ogif.kotae.ui.createanswer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ogif.kotae.R;
import com.ogif.kotae.data.repository.StorageRepository;
import com.ogif.kotae.databinding.ActivityCreateAnswerBinding;
import com.ogif.kotae.ui.createquestion.CreateQuestionActivity;
import com.ogif.kotae.ui.main.ImageAdapter;
import com.ogif.kotae.ui.questiondetail.AnswerViewModel;
import com.ogif.kotae.utils.model.UserUtils;
import com.ogif.kotae.utils.text.MarkdownUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class CreateAnswerActivity extends AppCompatActivity {

    private ActivityCreateAnswerBinding binding;
    private ActivityResultLauncher<Intent> answerActivityResultLauncher;
    private String content;
    private AnswerViewModel viewModel;

    private final int PICK_IMAGE_MULTIPLE = 1;
    private Uri imageUri;
    private ArrayList<Uri> imageList = new ArrayList<>();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference().child("answers");
    private int uploadCount = 0;
    private ImageAdapter imageAdapter;

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
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, getResources().getString(R.string.missing_answer_content), Toast.LENGTH_SHORT).show();
                return;
            }
            binding.fabPostAnswer.setEnabled(false);
            String id = getIntent().getStringExtra("questionId");

            List<String> imgIds = new ArrayList<>();
            uploadImage(imgIds,id);
            //this.viewModel.createAnswer(id, content);
            this.finish();
        });

        binding.btnAnswerImages.setOnClickListener(v-> {
            selectImage();
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

    public void selectImage() {
        if (imageList != null)
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

    public void uploadImage(List<String> imgIds, String id) {
        String currentDate, currentTime;
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        currentDate = simpleDateFormat.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        currentTime = timeFormat.format(calendarTime.getTime());

        String userId = UserUtils.getCachedUserId(CreateAnswerActivity.this);
        String name = userId + currentDate + currentTime + Integer.toString(uploadCount);

        StorageRepository storageRepository = new StorageRepository();
        storageRepository.uploadAnswerImages(imageList, uploadCount,
                CreateAnswerActivity.this, imageAdapter, name).addOnSuccessListener(new OnSuccessListener<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                imgIds.addAll(strings);
                // call update question to database here
                viewModel.createAnswer(id, content,imgIds);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

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

                    imageAdapter = new ImageAdapter(getApplicationContext(), imageList);
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
                        if (imageList.size() > 3) {
                            Toast.makeText(getApplicationContext(), "Only choose under 3 pictures", Toast.LENGTH_SHORT);
                            imageList.clear();
                        } else {
                            imageAdapter = new ImageAdapter(getApplicationContext(), imageList);
                            binding.gvQuestionImage.setAdapter(imageAdapter);
                            binding.gvQuestionImage.setVerticalSpacing(binding.gvQuestionImage.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) binding.gvQuestionImage
                                    .getLayoutParams();
                            mlp.setMargins(0, binding.gvQuestionImage.getHorizontalSpacing(), 0, 0);
                        }

                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
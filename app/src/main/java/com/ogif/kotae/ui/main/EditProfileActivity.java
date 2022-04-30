package com.ogif.kotae.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private static final String[] JOBS = {"Student", "Teacher"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tbEditProfile.setOnClickListener(v -> this.finish());

        ArrayAdapter<String> jobArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JOBS);
        binding.etEditJob.setAdapter(jobArrayAdapter);

        Intent intent = getIntent();
        String username = intent.getStringExtra("EDIT_USERNAME");
        String job = intent.getStringExtra("EDIT_JOB");
        int age = intent.getIntExtra("EDIT_AGE", 0);

        binding.etEditUsername.setText(username);

//        binding.etEditJob.setText(job);
//        if (job.toUpperCase().equals(JOBS[0].toUpperCase())) {
//            binding.etEditJob.setListSelection(1);
//        }
//        else binding.etEditJob.setListSelection(2);
        binding.etEditAge.setText(String.valueOf(age));
    }
}
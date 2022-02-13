package com.ogif.kotae.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private static final String[] JOBS = {"Student", "Teacher"};
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tvSignUpToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JOBS);
        binding.etSignUpJob.setAdapter(arrayAdapter);
    }
}
package com.ogif.kotae.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.databinding.ActivityLoginBinding;
import com.ogif.kotae.ui.main.UserActivity;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> {
            // TODO: Login user using Firebase Auth
            Intent intent = new Intent(v.getContext(), UserActivity.class);
            startActivity(intent);
            finish();
        });
        binding.tvLoginToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finishAffinity();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            reload(currentUser);
        }
    }

    private void reload(@NonNull FirebaseUser currentUser) {
        currentUser.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // TODO: Launch UserActivity if user, else AdminActivity
            }
        });
    }
}
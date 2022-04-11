package com.ogif.kotae.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.ogif.kotae.databinding.ActivityLoginBinding;
import com.ogif.kotae.ui.main.MainActivity;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.getUserLiveData().observe(this, (FirebaseUser user) -> {
            if (user == null) {
                binding.tvLoginError.setVisibility(View.VISIBLE);
                return;
            }
            // Store username and ID in shared preference
            viewModel.cacheCurrentUser(this);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            CharSequence email = binding.etLoginEmail.getText();
            CharSequence password = binding.etLoginPassword.getText();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                binding.tvLoginError.setVisibility(View.VISIBLE);
            } else {
                // toString() is safe, already checked with isEmpty()
                viewModel.login(email.toString(), password.toString());
            }
        });
        binding.tvLoginToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();
        });
    }
}
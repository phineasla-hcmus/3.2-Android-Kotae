package com.ogif.kotae.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
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

        this.viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        this.viewModel.getUserMutableLiveData().observe(this, user -> {
            if (user == null) {
                binding.tvLoginError.setVisibility(View.VISIBLE);
                return;
            }
            // TODO: check role
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
                this.viewModel.login(email.toString(), password.toString());
            }
        });
        binding.tvLoginToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();
        });
    }

    @Deprecated
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // FirebaseUser currentUser = auth.getCurrentUser();
        // if (currentUser != null) {
        //     reload(currentUser);
        // }
    }

    @Deprecated
    private void reload(@NonNull FirebaseUser currentUser) {
        currentUser.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // TODO: Launch MainActivity

            }
        });
    }
}
package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ogif.kotae.databinding.ActivityReEnterPasswordBinding;
import com.ogif.kotae.ui.auth.AuthViewModel;

public class ReEnterPasswordActivity extends AppCompatActivity {
    private ActivityReEnterPasswordBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReEnterPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Intent intent = getIntent();
        String username = intent.getStringExtra("CURRENT_USERNAME");

        binding.tvProfileUsername.setText(username);

        binding.btnCancelPassword.setOnClickListener(v -> this.finish());

        binding.btnSubmitPassword.setOnClickListener(v -> {
            viewModel.reAuthenticate(binding.etEnterPassword.getText().toString());
            viewModel.getAuthenticatedLiveData().observe(this, (Boolean authenticated) -> {
                if (!authenticated) {
                    binding.tvReEnterPasswordError.setText("Invalid password");
                    binding.tvReEnterPasswordError.setVisibility(View.VISIBLE);
                    return;
                }
                Intent intent1 = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent1);
                finish();
            });
        });
    }
}
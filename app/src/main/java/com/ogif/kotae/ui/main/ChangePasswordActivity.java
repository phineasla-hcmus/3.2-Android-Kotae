package com.ogif.kotae.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityChangePasswordBinding;
import com.ogif.kotae.ui.auth.AuthViewModel;
import com.ogif.kotae.utils.model.UserUtils;
import com.ogif.kotae.utils.text.TextValidator;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.tbChangePassword.setOnClickListener(v -> this.finish());

        binding.etNewPassword.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable password) {
                TextInputLayout til = binding.tilNewPassword;
                if (UserUtils.isPasswordValid(password) != UserUtils.OK) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_password));
                } else {
                    til.setErrorEnabled(false);
                }
            }
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            String newPass = binding.etNewPassword.getText().toString();
            String confirmPass = binding.etConfirmPassword.getText().toString();

            if (UserUtils.isPasswordValid(newPass) == UserUtils.OK && newPass.equals(confirmPass)) {
                viewModel.updatePassword(newPass);
                Toast.makeText(this, "Update password successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}
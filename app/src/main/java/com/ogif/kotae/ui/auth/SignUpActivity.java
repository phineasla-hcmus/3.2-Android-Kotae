package com.ogif.kotae.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.databinding.ActivitySignUpBinding;
import com.ogif.kotae.utils.model.UserUtils;
import com.ogif.kotae.utils.text.InputFilterMinMax;
import com.ogif.kotae.utils.text.TextValidator;

import java.util.Locale;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private static final String[] JOBS = {"Student", "Teacher"};
    private ActivitySignUpBinding binding;
    private AuthViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        this.viewModel.getMutableLiveData().observe(this, result -> {
            if (result.isFailed()) {
                binding.tvSignUpError.setText(getResources().getString(R.string.sign_up_error_existed));
                binding.tvSignUpError.setVisibility(View.VISIBLE);
                binding.btnSignUp.setEnabled(true);
                return;
            }
            startHomeActivity();
        });
        ArrayAdapter<String> jobArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JOBS);
        EditText[] ets = {binding.etSignUpEmail, binding.etSignUpUsername, binding.etSignUpJob, binding.etSignUpAge, binding.etSignUpPassword};

        binding.etSignUpUsername.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable username) {
                TextInputLayout til = binding.tilSignUpUsername;
                switch (UserUtils.isUsernameValid(username)) {
                    case UserUtils.INVALID_USERNAME_LENGTH:
                        til.setErrorEnabled(true);
                        til.setError(getResources().getString(R.string.et_error_username_length));
                        break;
                    case UserUtils.INVALID_USERNAME_FIRST_CHAR:
                        til.setErrorEnabled(true);
                        til.setError(getResources().getString(R.string.et_error_username_first_char));
                        break;
                    case UserUtils.INVALID_USERNAME_CHAR:
                        til.setErrorEnabled(true);
                        til.setError(getResources().getString(R.string.et_error_username_invalid_char));
                        break;
                    default:
                        til.setErrorEnabled(false);
                }
            }
        });
        binding.etSignUpJob.setAdapter(jobArrayAdapter);
        binding.etSignUpAge.setFilters(new InputFilter[]{new InputFilterMinMax(1, 100)});
        binding.etSignUpPassword.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable password) {
                TextInputLayout til = binding.tilSignUpPassword;
                if (UserUtils.isPasswordValid(password) != UserUtils.OK) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_password));
                } else {
                    til.setErrorEnabled(false);
                }
            }
        });
        binding.btnSignUp.setOnClickListener(v -> {
            for (EditText et : ets) {
                if (TextUtils.isEmpty(et.getText())) {
                    binding.tvSignUpError.setText(getResources().getString(R.string.sign_up_error_missing));
                    binding.tvSignUpError.setVisibility(View.VISIBLE);
                    return;
                }
            }
            String email = Objects.requireNonNull(binding.etSignUpEmail.getText()).toString();
            String username = Objects.requireNonNull(binding.etSignUpUsername.getText()).toString();
            String password = Objects.requireNonNull(binding.etSignUpPassword.getText()).toString();
            String job = Objects.requireNonNull(binding.etSignUpJob.getText()).toString();
            int age = Integer.parseInt(Objects.requireNonNull(binding.etSignUpAge.getText())
                    .toString());
            if (UserUtils.isUsernameValid(username) != UserUtils.OK
                    || UserUtils.isPasswordValid(password) != UserUtils.OK) {
                return;
            }
            binding.btnSignUp.setEnabled(false);
            this.viewModel.createUser(email, password,
                    new User(username, job.toLowerCase(Locale.ROOT), UserUtils.getYearOfBirth(age)));
        });
        binding.tvSignUpToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }
}
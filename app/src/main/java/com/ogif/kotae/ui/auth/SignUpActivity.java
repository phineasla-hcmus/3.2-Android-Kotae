package com.ogif.kotae.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ogif.kotae.Global;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivitySignUpBinding;
import com.ogif.kotae.util.text.InputFilterMinMax;
import com.ogif.kotae.util.text.TextValidator;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String[] JOBS = {"Student", "Teacher"};
    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();

        ArrayAdapter<String> jobArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JOBS);
        EditText[] ets = {binding.etSignUpEmail, binding.etSignUpUsername, binding.etSignUpJob, binding.etSignUpAge, binding.etSignUpPassword};
        Pattern usernamePattern = Pattern.compile("[A-Za-z0-9_]+");

        binding.etSignUpUsername.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable editable) {
                TextInputLayout til = binding.tilSignUpUsername;
                String username = editable.toString();
                if (username.length() < Global.USERNAME_MIN || username.length() > Global.USERNAME_MAX) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_username_first_char));
                } else if (Character.isDigit(username.charAt(0))) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_username_first_char));
                } else if (!usernamePattern.matcher(username).matches()) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_username_invalid_char));
                } else {
                    til.setErrorEnabled(false);
                }
            }
        });
        binding.etSignUpJob.setAdapter(jobArrayAdapter);
        binding.etSignUpAge.setFilters(new InputFilter[]{new InputFilterMinMax(1, 100)});
        binding.etSignUpPassword.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable editable) {
                TextInputLayout til = binding.tilSignUpPassword;
                if (editable.length() < Global.PASSWORD_MIN || editable.length() > Global.PASSWORD_MAX) {
                    til.setErrorEnabled(true);
                    til.setError(getResources().getString(R.string.et_error_password));
                } else {
                    til.setErrorEnabled(false);
                }
            }
        });
        binding.btnSignUp.setOnClickListener(v -> {
            for (EditText et : ets) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    binding.tvSignUpError.setVisibility(View.VISIBLE);
                    return;
                }
            }
            int usernameLength = binding.etSignUpUsername.length();
            int passwordLength = binding.etSignUpPassword.length();
            if (usernameLength < Global.USERNAME_MIN || usernameLength > Global.USERNAME_MAX
                    || passwordLength < Global.PASSWORD_MIN || passwordLength > Global.PASSWORD_MAX) {
                return;
            }


        });
        binding.tvSignUpToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();
        });
    }
}
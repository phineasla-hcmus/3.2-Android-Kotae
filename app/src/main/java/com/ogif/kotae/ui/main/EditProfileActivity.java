package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityEditProfileBinding;
import com.ogif.kotae.ui.ProfileViewModel;
import com.ogif.kotae.utils.model.UserUtils;
import com.ogif.kotae.utils.text.InputFilterMinMax;
import com.ogif.kotae.utils.text.TextValidator;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private static final String[] JOBS = {"Student", "Teacher"};
    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        EditText[] ets = {binding.etEditUsername,
                binding.etEditJob,
                binding.etEditAge};

        binding.tbEditProfile.setOnClickListener(v -> this.finish());

        ArrayAdapter<String> jobArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JOBS);
        binding.etEditJob.setAdapter(jobArrayAdapter);

        Intent intent = getIntent();
        String username = intent.getStringExtra("EDIT_USERNAME");
        String job = intent.getStringExtra("EDIT_JOB");
        int age = intent.getIntExtra("EDIT_AGE", 0);

        binding.etEditUsername.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(Editable username) {
                TextInputLayout til = binding.tilEditUsername;
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
        binding.etEditUsername.setText(username);

        if (job.toLowerCase().equals(JOBS[0].toLowerCase())) {
            binding.etEditJob.setText(JOBS[0], false);
        } else binding.etEditJob.setText(JOBS[1], false);

        binding.etEditAge.setFilters(new InputFilter[]{new InputFilterMinMax(1, 100)});
        binding.etEditAge.setText(String.valueOf(age));

        binding.btnEditProfile.setOnClickListener(v -> {
            for (EditText et : ets) {
                if (TextUtils.isEmpty(et.getText())) {
                    binding.tvEditProfileError.setText(getResources().getString(R.string.field_error_missing));
                    binding.tvEditProfileError.setVisibility(View.VISIBLE);
                    return;
                }
            }
            String newUsername = Objects.requireNonNull(binding.etEditUsername.getText()).toString();
            String newJob = Objects.requireNonNull(binding.etEditJob.getText()).toString();
            int newAge = Integer.parseInt(Objects.requireNonNull(binding.etEditAge.getText())
                    .toString());
            if (UserUtils.isUsernameValid(newUsername) != UserUtils.OK) {
                return;
            }
            binding.btnEditProfile.setEnabled(false);

            viewModel.updateUser(newUsername, newJob, newAge);
            viewModel.getUpdatedLiveData().observe(this, (Boolean updated) -> {
                if (updated) {
                    Toast.makeText(this, "Edit profile successfully", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
        });
    }
}
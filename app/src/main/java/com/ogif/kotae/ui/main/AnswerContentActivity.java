package com.ogif.kotae.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityAnswerContentBinding;
import com.ogif.kotae.utils.model.MarkdownUtils;

public class AnswerContentActivity extends AppCompatActivity {
    private ActivityAnswerContentBinding binding;
    private View view;
    private EditText etMarkdown;
    private TextView tvPreview;
    private Button btnBold, btnItalic, btnCode, btnHeading1, btnHeading2;
    private TabLayout tabLayout;
    private ExtendedFloatingActionButton fabSaveAnswer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAnswerContentBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        toolbar = binding.tbAnswerContent;
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create answer");

        etMarkdown = binding.etMarkdown;
        tvPreview = binding.tvPreview;

        btnBold = binding.btnBold;
        btnItalic = binding.btnItalic;
        btnCode = binding.btnCode;
        btnHeading1 = binding.btnHeading1;
        btnHeading2 = binding.btnHeading2;

        tabLayout = binding.tabLayout;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabChanged(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fabSaveAnswer = binding.fabSaveAnswer;
        fabSaveAnswer.setOnClickListener(v -> {
            saveDraftContent();
        });

        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {
            etMarkdown.setText(text);
            MarkdownUtils.setMarkdown(getApplicationContext(), text, binding.tvPreview);
        }

    }

    // pass answer content to CreateAnswerActivity
    private void saveDraftContent() {
        String content = binding.etMarkdown.getText().toString();
        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, content);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onTabChanged(int position) {
        // get a reference to the tabs container view
        LinearLayout ll = (LinearLayout) tabLayout.getChildAt(0);
        // get the child view at the position of the currently selected tab and set selected to false
        ll.getChildAt(tabLayout.getSelectedTabPosition()).setSelected(false);
        // get the child view at the new selected position and set selected to true
        ll.getChildAt(position).setSelected(true);
        // move the selection indicator
        tabLayout.setScrollPosition(position, 0, true);

        switch (position) {
            case 0: {
                binding.tilMarkdownInput.setVisibility(View.VISIBLE);
                binding.tvPreview.setVisibility(View.GONE);
                binding.etMarkdown.requestFocus();

                // show keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                break;
            }
            case 1: {
                binding.tilMarkdownInput.setVisibility(View.GONE);
                binding.tvPreview.setVisibility(View.VISIBLE);

                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                refreshPreview();
                break;
            }
        }
    }

    private void refreshPreview() {
        if (TextUtils.isEmpty(binding.etMarkdown.getText().toString())) {
            binding.tvPreview.setText(R.string.no_preview);
        } else {
            MarkdownUtils.setMarkdown(getApplicationContext(), binding.etMarkdown.getText().toString(), binding.tvPreview);
        }
    }
}
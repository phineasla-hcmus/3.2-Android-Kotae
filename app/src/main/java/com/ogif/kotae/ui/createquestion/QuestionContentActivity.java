package com.ogif.kotae.ui.createquestion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.ogif.kotae.R;
import com.ogif.kotae.databinding.ActivityQuestionContentBinding;
import com.ogif.kotae.utils.text.MarkdownUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.Objects;

public class QuestionContentActivity extends AppCompatActivity {

    private ActivityQuestionContentBinding binding;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionContentBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        binding.toolbarQuestionContent.setOnClickListener(v -> this.finish());

        binding.toolbarQuestionContent.getMenu().getItem(0).setOnMenuItemClickListener(v -> {
            saveDraftContent();
            return true;
        });

        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {
            binding.etMarkdown.setText(text);
            MarkdownUtils.setMarkdown(this, text, binding.tvPreview);
        }

        MarkdownUtils.buildButtonsScroll(getResources(), this, binding.llBtnEdit, binding.etMarkdown);

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            if (isOpen && binding.etMarkdown.hasFocus()) {
                binding.scrollLayout.smoothScrollTo(0, binding.scrollView.getBottom());
            }
        });
    }

    // pass answer content to CreateQuestionActivity
    private void saveDraftContent() {
        String content = Objects.requireNonNull(binding.etMarkdown.getText()).toString();
        Log.d("DRAFT", content);
        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, content);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onTabChanged(int position) {
        // get a reference to the tabs container view
        LinearLayout ll = (LinearLayout) binding.tabLayout.getChildAt(0);
        // get the child view at the position of the currently selected tab and set selected to false
        ll.getChildAt(binding.tabLayout.getSelectedTabPosition()).setSelected(false);
        // get the child view at the new selected position and set selected to true
        ll.getChildAt(position).setSelected(true);
        // move the selection indicator
        binding.tabLayout.setScrollPosition(position, 0, true);

        switch (position) {
            case 0: {
                binding.tilMarkdownInput.setVisibility(View.VISIBLE);
                binding.tvPreview.setVisibility(View.GONE);
                binding.etMarkdown.requestFocus();
                binding.scrollView.setVisibility(View.VISIBLE);

                // show keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                break;
            }
            case 1: {
                binding.tilMarkdownInput.setVisibility(View.GONE);
                binding.tvPreview.setVisibility(View.VISIBLE);
                binding.scrollView.setVisibility(View.GONE);

                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                refreshPreview();
                break;
            }
        }
    }

    private void refreshPreview() {
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.etMarkdown.getText()).toString())) {
            binding.tvPreview.setText(R.string.no_preview);
        } else {
            MarkdownUtils.setMarkdown(this, binding.etMarkdown.getText().toString(), binding.tvPreview);
        }
    }

}
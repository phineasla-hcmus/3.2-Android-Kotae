package com.ogif.kotae.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ogif.kotae.databinding.ActivityQuestionContentBinding;

import org.commonmark.node.Node;


import io.noties.markwon.Markwon;

import com.ogif.kotae.R;

public class QuestionContentActivity extends AppCompatActivity {

    private EditText etMarkdown;
    private TextView tvPreview;
    private ActivityQuestionContentBinding binding;
    private Button btnBold, btnItalic, btnCode, btnHeading1, btnHeading2;
    private TabLayout tabLayout;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionContentBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

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
            setMarkdown(binding.etMarkdown.getText().toString());
        }
    }

    private void setMarkdown(String content) {
        // obtain an instance of Markwon
        final Markwon markwon = Markwon.create(getApplicationContext());

        // parse markdown to commonmark-java Node
        final Node node = markwon.parse(content);

        // create styled text from parsed Node
        final Spanned markdown = markwon.render(node);

        // use it on a TextView
        markwon.setParsedMarkdown(tvPreview, markdown);
    }

}
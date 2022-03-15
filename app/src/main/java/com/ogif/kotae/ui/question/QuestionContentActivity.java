package com.ogif.kotae.ui.question;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ogif.kotae.databinding.ActivityQuestionContentBinding;

import org.commonmark.node.Node;


import io.noties.markwon.Markwon;

import com.ogif.kotae.R;
import com.ogif.kotae.utils.model.MarkdownUtils;

public class QuestionContentActivity extends AppCompatActivity {

    private EditText etMarkdown;
    private TextView tvPreview;
    private ActivityQuestionContentBinding binding;
    private TabLayout tabLayout;
    private ExtendedFloatingActionButton fabSaveQuestion;
    private View view;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionContentBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        toolbar = binding.tbQuestionContent;
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Question content");

        etMarkdown = binding.etMarkdown;
        tvPreview = binding.tvPreview;

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

        fabSaveQuestion = binding.fabSaveQuestion;
        fabSaveQuestion.setOnClickListener(v -> {
            saveDraftContent();
        });

        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {
            etMarkdown.setText(text);
            MarkdownUtils.setMarkdown(getApplicationContext(), text, binding.tvPreview);
        }

        buildButtonsScroll();
    }

    // pass answer content to CreateQuestionActivity
    private void saveDraftContent() {
        String content = binding.etMarkdown.getText().toString();
        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, content);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void insertText(String text) {
        binding.etMarkdown.getText().insert(binding.etMarkdown.getSelectionStart(), text);
    }

    private void buildButtonsScroll() {
        int width = (int) getResources().getDimension(R.dimen.btn_edit_width);
        int height = (int) getResources().getDimension(R.dimen.btn_edit_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);

        String[] labels = {"B", "I", "`", "h1", "h2", "<>", "{}", "[]", "()", "li"};
        String[] mds = {"**bold**", "_italic_", "\n> blockquote", "\n# h1", "\n## h2", "$${a \\bangle b}$$", "$${c \\brace d}$$", "$${e \\brack f}$$", "$${g \\choose h}$$", "\n- item"};

        for (int i = 0; i < labels.length; i++) {
            final Button btnEdit = new Button(QuestionContentActivity.this);
            btnEdit.setText(labels[i]);
            btnEdit.setTextSize(12f);
            btnEdit.setAllCaps(false);
            btnEdit.setBackgroundColor(MaterialColors.getColor(btnEdit, R.attr.colorPrimary));
            btnEdit.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            btnEdit.setLayoutParams(layoutParams);
            btnEdit.setTag(i);
            int finalI = i;
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertText(mds[finalI]);
                }
            });
            binding.llBtnEdit.addView(btnEdit);
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
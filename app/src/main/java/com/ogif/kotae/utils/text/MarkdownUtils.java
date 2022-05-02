package com.ogif.kotae.utils.text;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.color.MaterialColors;
import com.ogif.kotae.R;

import java.util.Objects;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;

public class MarkdownUtils {
    public static void setMarkdown(Context context, String content, TextView contentView) {
        // obtain an instance of Markwon
        final Markwon markwon = Markwon.builder(context)
                // required plugin to support inline parsing
                .usePlugin(MarkwonInlineParserPlugin.create())
                .usePlugin(JLatexMathPlugin.create(contentView.getTextSize(), new JLatexMathPlugin.BuilderConfigure() {
                    @Override
                    public void configureBuilder(@NonNull JLatexMathPlugin.Builder builder) {
                        // ENABLE inlines
                        builder.inlinesEnabled(true);
                    }
                }))
                .build();

        // use it on a Text View
        markwon.setMarkdown(contentView, content);
    }

    public static void buildButtonsScroll(Resources resources, Context context, LinearLayout llBtnEdit, EditText etMarkdown) {
        int width = (int) resources.getDimension(R.dimen.btn_edit_width);
        int height = (int) resources.getDimension(R.dimen.btn_edit_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        String[] labels = {"**B**", "_I_", "\n> block", "h1", "h2", "li", "$$x^2$$", "$$x_{2}$$", "$${a \\bangle b}$$", "$${c \\brace d}$$", "$${e \\brack f}$$", "$${g \\choose h}$$"};
        String[] mds = {"**Bold**", "_Italic_", "\n> blockquote", "\n# h1", "\n## h2", "\n- item", "$$x^2$$", "$$x_{2}$$", "$${a \\bangle b}$$", "$${c \\brace d}$$", "$${e \\brack f}$$", "$${g \\choose h}$$"};

        for (int i = 0; i < mds.length; i++) {
            final Button btnEdit = new Button(context);
            MarkdownUtils.setMarkdown(context, labels[i], btnEdit);
            btnEdit.setTextSize(10f);
            btnEdit.setAllCaps(false);
            btnEdit.setBackgroundColor(MaterialColors.getColor(btnEdit, R.attr.colorPrimary));
            btnEdit.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            if (i == 0 || i == 1) {
                int w = width / 2;
                btnEdit.setLayoutParams(new ViewGroup.LayoutParams(w, height));
            } else btnEdit.setLayoutParams(layoutParams);
            btnEdit.setTag(i);
            int finalI = i;
            btnEdit.setOnClickListener(view -> insertText(mds[finalI], etMarkdown));
            llBtnEdit.addView(btnEdit);
        }
    }

    private static void insertText(String text, EditText etMarkdown) {
        Objects.requireNonNull(etMarkdown.getText()).insert(etMarkdown.getSelectionStart(), text);
    }
}

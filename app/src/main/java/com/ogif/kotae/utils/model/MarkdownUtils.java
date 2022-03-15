package com.ogif.kotae.utils.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.commonmark.node.Node;

import java.util.concurrent.Executors;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.ext.latex.JLatexMathTheme;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;
import ru.noties.jlatexmath.JLatexMathDrawable;

public class MarkdownUtils {
    public static void setMarkdown(Context context, String content, TextView contentView) {
        // obtain an instance of Markwon
//        final Markwon markwon = Markwon.create(context);
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

//        // parse markdown to commonmark-java Node
//        final Node node = markwon.parse(content);
//
//        // create styled text from parsed Node
//        final Spanned markdown = markwon.render(node);

        final String markdown = "# Latex inline\n" + "hey = $$" + "{a \\bangle b} {c \\brace d} {e \\brack f} {g \\choose h}" + "$$, \n" + "that's it!";

        // use it on a Text View
        markwon.setMarkdown(contentView, content);
    }
}

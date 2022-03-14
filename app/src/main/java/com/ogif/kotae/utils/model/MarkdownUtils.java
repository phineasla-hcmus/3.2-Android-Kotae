package com.ogif.kotae.utils.model;

import android.content.Context;
import android.text.Spanned;
import android.widget.TextView;

import org.commonmark.node.Node;

import io.noties.markwon.Markwon;

public class MarkdownUtils {
    public static void setMarkdown(Context context, String content, TextView contentView) {
        // obtain an instance of Markwon
        final Markwon markwon = Markwon.create(context);

        // parse markdown to commonmark-java Node
        final Node node = markwon.parse(content);

        // create styled text from parsed Node
        final Spanned markdown = markwon.render(node);

        // use it on a Text View
        markwon.setParsedMarkdown(contentView, markdown);
    }
}

package com.ogif.kotae.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ReplacementSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ogif.kotae.R;
import com.ogif.kotae.editor.shared.BlockQuoteEditHandler;
import com.ogif.kotae.editor.shared.CodeEditHandler;
import com.ogif.kotae.editor.shared.HeadingEditHandler;
import com.ogif.kotae.editor.shared.LinkEditHandler;
import com.ogif.kotae.editor.shared.StrikethroughEditHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.SoftBreakAddsNewLinePlugin;
import io.noties.markwon.core.spans.EmphasisSpan;
import io.noties.markwon.core.spans.StrongEmphasisSpan;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import io.noties.markwon.editor.PersistedSpans;
import io.noties.markwon.editor.handler.EmphasisEditHandler;
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler;

public class QuestionContentActivity extends AppCompatActivity {

    private static EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_content);

        final Markwon markwon = Markwon.builder(getApplicationContext())
                .usePlugin(SoftBreakAddsNewLinePlugin.create())
                .build();

        final MarkwonEditor editor = MarkwonEditor.builder(markwon)
                .punctuationSpan(QuestionContentActivity.HidePunctuationSpan.class, new PersistedSpans.SpanFactory<QuestionContentActivity.HidePunctuationSpan>() {
                    @NonNull
                    @Override
                    public QuestionContentActivity.HidePunctuationSpan create() {
                        return new QuestionContentActivity.HidePunctuationSpan();
                    }
                })
                .useEditHandler(new EmphasisEditHandler())
                .useEditHandler(new StrongEmphasisEditHandler())
                .useEditHandler(new StrikethroughEditHandler())
                .useEditHandler(new CodeEditHandler())
                .useEditHandler(new BlockQuoteEditHandler())
                .useEditHandler(new LinkEditHandler(new LinkEditHandler.OnClick() {
                    @Override
                    public void onClick(@NonNull View widget, @NonNull String link) {
//          Debug.e("clicked: %s", link);
                    }
                }))
                .useEditHandler(new HeadingEditHandler())
                .build();

        editText = (EditText) findViewById(R.id.edit_text);
        // for links to be clickable
        //   NB! markwon MovementMethodPlugin cannot be used here as editor do not execute `beforeSetText`)
        editText.setMovementMethod(LinkMovementMethod.getInstance());

        editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));

        initBottomBar();
    }

    private static class HidePunctuationSpan extends ReplacementSpan {

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            // last space (which is swallowed until next non-space character appears)
            // block quote
            // code tick

//      Debug.i("text: '%s', %d-%d (%d)", text.subSequence(start, end), start, end, text.length());
            if (end == text.length()) {
                // TODO: find first non-space character (not just first one because commonmark allows
                //  arbitrary (0-3) white spaces before content starts

                //  TODO: if all white space - render?
                final char c = text.charAt(start);
                if ('#' == c
                        || '>' == c
                        || '-' == c // TODO: not thematic break
                        || '+' == c
                        // `*` is fine but only for a list
                        || isBulletList(text, c, start, end)
                        || Character.isDigit(c) // assuming ordered list (replacement should only happen for ordered lists)
                        || Character.isWhitespace(c)) {
                    return (int) (paint.measureText(text, start, end) + 0.5F);
                }
            }
            return 0;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            // will be called only when getSize is not 0 (and if it was once reported as 0...)
            if (end == text.length()) {

                // if first non-space is `*` then check for is bullet
                //  else `**` would be still rendered at the end of the emphasis
                if (text.charAt(start) == '*'
                        && !isBulletList(text, '*', start, end)) {
                    return;
                }

                // TODO: inline code last tick received here, handle it (do not highlight)
                //  why can't we have reported width in this method for supplied text?

                // let's use color to make it distinct from the rest of the text for demonstration purposes
                paint.setColor(0xFFff0000);

                canvas.drawText(text, start, end, x, y, paint);
            }
        }

        private static boolean isBulletList(@NonNull CharSequence text, char firstChar, int start, int end) {
            return '*' == firstChar
                    && ((end - start == 1) || (Character.isWhitespace(text.charAt(start + 1))));
        }
    }

    private void initBottomBar() {
        // all except block-quote wraps if have selection, or inserts at current cursor position
        Button bold = findViewById(R.id.bold);
        Button italic = findViewById(R.id.italic);
        Button strike = findViewById(R.id.strike);
        Button quote = findViewById(R.id.quote);
        Button code = findViewById(R.id.code);

        addSpan(bold, new StrongEmphasisSpan());
        addSpan(italic, new EmphasisSpan());
        addSpan(strike, new StrikethroughSpan());

        bold.setOnClickListener(new QuestionContentActivity.InsertOrWrapClickListener(editText, "**"));
        italic.setOnClickListener(new QuestionContentActivity.InsertOrWrapClickListener(editText, "_"));
        strike.setOnClickListener(new QuestionContentActivity.InsertOrWrapClickListener(editText, "~~"));
        code.setOnClickListener(new QuestionContentActivity.InsertOrWrapClickListener(editText, "`"));

        quote.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                int start = editText.getSelectionStart();
                int end = editText.getSelectionEnd();
                if (start >= 0) {
                    if (start == end) {
                        editText.getText().insert(start, (CharSequence)"> ");
                    } else {
                        List newLines = (List)(new ArrayList(3));
                        newLines.add(start);
                        String text = editText.getText().subSequence(start, end).toString();

                        int index = text.indexOf('\n');
                        while (index != -1) {
                            newLines.add(start + index + 1);
                            index = text.indexOf('\n', index + 1);
                        }

                        int i = ((Collection)newLines).size();
                        --i;

                        for(boolean var8 = false; i >= 0; --i) {
                            editText.getText().insert(((Number)newLines.get(i)).intValue(), (CharSequence)"> ");
                        }
                    }

                }
            }
        }));
    }

    private final void addSpan(TextView textView, Object... spans) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText());
        int end = builder.length();
        Object[] var7 = spans;
        int var8 = spans.length;

        for(int var6 = 0; var6 < var8; ++var6) {
            Object span = var7[var6];
            builder.setSpan(span, 0, end, 33);
        }

        textView.setText((CharSequence)builder);
    }

    private class InsertOrWrapClickListener implements View.OnClickListener {
        private final EditText editText;
        private final String text;

        private InsertOrWrapClickListener(EditText editText, String text) {
            super();
            this.editText = editText;
            this.text = text;
        }

        @Override
        public void onClick(View view) {
            int start = this.editText.getSelectionStart();
            int end = this.editText.getSelectionEnd();
            if (start >= 0) {
                if (start == end) {
                    this.editText.getText().insert(start, (CharSequence)this.text);
                } else {
                    this.editText.getText().insert(end, (CharSequence)this.text);
                    this.editText.getText().insert(start, (CharSequence)this.text);
                }

            }
        }
    }
}
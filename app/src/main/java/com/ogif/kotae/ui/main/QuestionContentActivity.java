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
import android.widget.ImageButton;
import android.widget.TextView;
import com.ogif.kotae.databinding.ActivityQuestionContentBinding;
import com.ogif.kotae.editor.BlockQuoteEditHandler;
import com.ogif.kotae.editor.CodeEditHandler;
import com.ogif.kotae.editor.HeadingEditHandler;
import com.ogif.kotae.editor.LinkEditHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.SoftBreakAddsNewLinePlugin;
import io.noties.markwon.core.spans.EmphasisSpan;
import io.noties.markwon.core.spans.StrongEmphasisSpan;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
//import io.noties.markwon.editor.PersistedSpans;
import io.noties.markwon.editor.handler.EmphasisEditHandler;
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler;

public class QuestionContentActivity extends AppCompatActivity {

    private EditText editText;
    private ActivityQuestionContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        final Markwon markwon = Markwon.builder(getApplicationContext())
//                .usePlugin(SoftBreakAddsNewLinePlugin.create())
//                .build();
//
//        final MarkwonEditor editor = MarkwonEditor.builder(markwon)
//                .punctuationSpan(QuestionContentActivity.HidePunctuationSpan.class, HidePunctuationSpan::new)
//                .useEditHandler(new EmphasisEditHandler())
//                .useEditHandler(new StrongEmphasisEditHandler())
////                .useEditHandler(new StrikethroughEditHandler())
//                .useEditHandler(new CodeEditHandler())
//                .useEditHandler(new BlockQuoteEditHandler())
//                .useEditHandler(new LinkEditHandler((widget, link) -> {
////          Debug.e("clicked: %s", link);
//                }))
//                .useEditHandler(new HeadingEditHandler())
//                .build();

        editText = binding.etMarkdown;
//        // for links to be clickable
//        //   NB! markwon MovementMethodPlugin cannot be used here as editor do not execute `beforeSetText`)
//        editText.setMovementMethod(LinkMovementMethod.getInstance());
//
//        editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));
//
//        initBottomBar();
    }

//    private static class HidePunctuationSpan extends ReplacementSpan {
//
//        @Override
//        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
//            // last space (which is swallowed until next non-space character appears)
//            // block quote
//            // code tick
//
////      Debug.i("text: '%s', %d-%d (%d)", text.subSequence(start, end), start, end, text.length());
//            if (end == text.length()) {
//                // TODO: find first non-space character (not just first one because commonmark allows
//                //  arbitrary (0-3) white spaces before content starts
//
//                //  TODO: if all white space - render?
//                final char c = text.charAt(start);
//                if ('#' == c
//                        || '>' == c
//                        || '-' == c // TODO: not thematic break
//                        || '+' == c
//                        // `*` is fine but only for a list
//                        || isBulletList(text, c, start, end)
//                        || Character.isDigit(c) // assuming ordered list (replacement should only happen for ordered lists)
//                        || Character.isWhitespace(c)) {
//                    return (int) (paint.measureText(text, start, end) + 0.5F);
//                }
//            }
//            return 0;
//        }
//
//        @Override
//        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
//            // will be called only when getSize is not 0 (and if it was once reported as 0...)
//            if (end == text.length()) {
//
//                // if first non-space is `*` then check for is bullet
//                //  else `**` would be still rendered at the end of the emphasis
//                if (text.charAt(start) == '*'
//                        && !isBulletList(text, '*', start, end)) {
//                    return;
//                }
//
//                // TODO: inline code last tick received here, handle it (do not highlight)
//                //  why can't we have reported width in this method for supplied text?
//
//                // let's use color to make it distinct from the rest of the text for demonstration purposes
//                paint.setColor(0xFFff0000);
//
//                canvas.drawText(text, start, end, x, y, paint);
//            }
//        }
//
//        private static boolean isBulletList(@NonNull CharSequence text, char firstChar, int start, int end) {
//            return '*' == firstChar
//                    && ((end - start == 1) || (Character.isWhitespace(text.charAt(start + 1))));
//        }
//    }
//
//    private void initBottomBar() {
//        // all except block-quote wraps if have selection, or inserts at current cursor position
//        Button btnBold = binding.btnBold;
//        Button btnItalic = binding.btnItalic;
//        Button btnCode = binding.btnCode;
//        Button btnHeading1 = binding.btnHeading1;
//        Button btnHeading2 = binding.btnHeading2;
//
//        addSpan(btnBold, new StrongEmphasisSpan());
//        addSpan(btnItalic, new EmphasisSpan());
////        addSpan(strike, new StrikethroughSpan());
//
//        btnBold.setOnClickListener(new InsertOrWrapClickListener(editText, "**"));
//        btnItalic.setOnClickListener(new InsertOrWrapClickListener(editText, "_"));
////        strike.setOnClickListener(new InsertOrWrapClickListener(editText, "~~"));
//        btnCode.setOnClickListener(new InsertOrWrapClickListener(editText, "`"));
//        btnHeading1.setOnClickListener(new InsertOrWrapClickListener(editText, "# "));
//        btnHeading2.setOnClickListener(new InsertOrWrapClickListener(editText, "## "));
//
////        quote.setOnClickListener((it -> {
////            int start = editText.getSelectionStart();
////            int end = editText.getSelectionEnd();
////            if (start >= 0) {
////                if (start == end) {
////                    editText.getText().insert(start, "> ");
////                } else {
////                    List<Integer> newLines = new ArrayList<>(3);
////                    newLines.add(start);
////                    String text = editText.getText().subSequence(start, end).toString();
////
////                    int index = text.indexOf('\n');
////                    while (index != -1) {
////                        newLines.add(start + index + 1);
////                        index = text.indexOf('\n', index + 1);
////                    }
////
////                    int i = ((Collection<Integer>)newLines).size();
////                    --i;
////
////                    for(; i >= 0; --i) {
////                        editText.getText().insert(((Number)newLines.get(i)).intValue(), "> ");
////                    }
////                }
////
////            }
////        }));
//    }
//
//    private void addSpan(TextView textView, Object... spans) {
//        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText());
//        int end = builder.length();
//
//        for (Object span : spans) {
//            builder.setSpan(span, 0, end, 33);
//        }
//
//        textView.setText(builder);
//    }
//
//    private static class InsertOrWrapClickListener implements View.OnClickListener {
//        private final EditText editText;
//        private final String text;
//
//        private InsertOrWrapClickListener(EditText editText, String text) {
//            super();
//            this.editText = editText;
//            this.text = text;
//        }
//
//        @Override
//        public void onClick(View view) {
//            int start = this.editText.getSelectionStart();
//            int end = this.editText.getSelectionEnd();
//            if (start >= 0) {
//                if (start != end) {
//                    this.editText.getText().insert(end, this.text);
//                }
//                this.editText.getText().insert(start, this.text);
//
//            }
//        }
//    }
}
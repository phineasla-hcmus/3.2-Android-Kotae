package com.ogif.kotae.ui.comment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ogif.kotae.R;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;


public class CommentFragment extends BottomSheetDialogFragment {
    private Context context;
    private String postId;
    private RecyclerView recyclerView;

    public CommentFragment(Context context, String postId) {
        this.context = context;
        this.postId = postId;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_comment);

        recyclerView = dialog.findViewById(R.id.recycler_view_bottom_sheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        EditText etContent = dialog.findViewById(R.id.et_comment_input);

        dialog.findViewById(R.id.btn_comment_send).setOnClickListener(v -> {
            if (TextUtils.isEmpty(etContent.getText().toString())) {
                Toast.makeText(context, context.getResources()
                        .getString(R.string.comment_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            ((QuestionDetailActivity) context).createComment(postId, etContent.getText()
                    .toString());
            dialog.hide();
            etContent.setText("");
        });

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            // setup full height
            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            bottomSheet.setLayoutParams(layoutParams);

            ((QuestionDetailActivity) context).updateComments(recyclerView, postId);
        });

        return dialog;
    }
}
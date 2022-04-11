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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ogif.kotae.R;
import com.ogif.kotae.ui.comment.adapter.CommentAdapter;
import com.ogif.kotae.utils.model.UserUtils;


public class CommentFragment extends BottomSheetDialogFragment {
    private Context context;
    private String postId;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private CommentViewModel commentViewModel;

    public CommentFragment(Context context, String postId) {
        this.context = context;
        this.postId = postId;
        this.commentAdapter = new CommentAdapter(context);

    }

    public void createComment(@NonNull String postId, @NonNull String content) {
        commentViewModel.createComment(postId, content);
    }

    public void updateComments(@NonNull RecyclerView recyclerView, @NonNull String postId) {
        recyclerView.setAdapter(commentAdapter);
        commentViewModel.getComments(postId);

        commentViewModel
                .getCommentLiveData()
                .observe(this, comments -> commentAdapter.updateComments(comments));
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String userId = UserUtils.getCachedUserId(context);
        String username = UserUtils.getCachedUsername(context);
        CommentViewModelFactory commentViewModelFactory = new CommentViewModelFactory(userId, username);
        this.commentViewModel = new ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel.class);

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
            createComment(postId, etContent.getText().toString());
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

            updateComments(recyclerView, postId);
        });

        return dialog;
    }
}
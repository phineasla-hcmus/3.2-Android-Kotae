package com.ogif.kotae.ui.comment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ogif.kotae.Global;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.repository.CommentPagingSource;
import com.ogif.kotae.ui.comment.adapter.CommentAdapter;
import com.ogif.kotae.utils.model.CommentComparator;
import com.ogif.kotae.utils.model.UserUtils;


public class CommentFragment extends BottomSheetDialogFragment {
    public static final String BUNDLE_POST_ID = "postId";

    private RecyclerView recyclerView;
    private AppCompatImageButton sendComment;
    private EditText inputComment;
    private CommentAdapter commentAdapter;
    private CommentViewModel commentViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        String userId = UserUtils.getCachedUserId(requireActivity());
        String username = UserUtils.getCachedUsername(requireActivity());

        Bundle args = this.getArguments();
        assert args != null;

        String postId = args.getString(BUNDLE_POST_ID);

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_comment);

        recyclerView = dialog.findViewById(R.id.recycler_view_bottom_sheet);
        sendComment = dialog.findViewById(R.id.btn_comment_send);
        inputComment = dialog.findViewById(R.id.et_comment_input);
        assert recyclerView != null;
        assert sendComment != null;
        assert inputComment != null;

        commentAdapter = new CommentAdapter(requireActivity(), new CommentComparator());
        CommentViewModelFactory commentViewModelFactory = new CommentViewModelFactory(userId, username, postId);
        this.commentViewModel = new ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        observe();

        sendComment.setOnClickListener(v -> {
            if (TextUtils.isEmpty(inputComment.getText().toString())) {
                FragmentActivity fragmentActivity = requireActivity();
                Toast.makeText(fragmentActivity, fragmentActivity.getResources()
                        .getString(R.string.comment_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            createComment(inputComment.getText().toString());
            dialog.hide();
            inputComment.setText("");
        });

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            // Setup full height
            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            bottomSheet.setLayoutParams(layoutParams);

            updateComments(recyclerView);
        });

        return dialog;
    }

    public void createComment(@NonNull String content) {
        commentViewModel.createComment(content);
    }

    public void updateComments(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(commentAdapter);
        commentViewModel.getComments();
    }

    public void observe() {
        commentViewModel.getPagingCommentLiveData().observe(this, commentPagingData -> {
            commentAdapter.submitData(getLifecycle(), commentPagingData);
        });
    }

    @NonNull
    public static CommentFragment newInstance(@NonNull String postId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }
}
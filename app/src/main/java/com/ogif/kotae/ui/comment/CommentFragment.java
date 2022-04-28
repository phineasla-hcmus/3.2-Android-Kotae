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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.ui.comment.adapter.CommentAdapter;
import com.ogif.kotae.utils.model.UserUtils;
import com.ogif.kotae.utils.ui.LazyLoadScrollListener;


public class CommentFragment extends BottomSheetDialogFragment {
    public static final String BUNDLE_POST_ID = "postId";
    public static final String BUNDLE_POST_AUTHOR_ID = "postAuthorId";

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private CommentViewModel viewModel;

    private EditText inputComment;
    private AppCompatImageButton sendComment;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Bundle args = this.getArguments();
        assert args != null;

        String postId = args.getString(BUNDLE_POST_ID);
        String postAuthorId = args.getString(BUNDLE_POST_AUTHOR_ID);
        String userId = UserUtils.getCachedUserId(requireActivity());
        String username = UserUtils.getCachedUsername(requireActivity());

        // Binding stuffs
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_comment);
        recyclerView = dialog.findViewById(R.id.recycler_view_bottom_sheet);
        sendComment = dialog.findViewById(R.id.btn_comment_send);
        inputComment = dialog.findViewById(R.id.et_comment_input);
        assert recyclerView != null;
        assert sendComment != null;
        assert inputComment != null;

        CommentViewModelFactory commentViewModelFactory = new CommentViewModelFactory(userId, username, postId);
        this.viewModel = new ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel.class);

        // Set adapter and LazyLoadScrollListener
        adapter = new CommentAdapter(requireActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new LazyLoadScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                viewModel.getComments();
            }
        });

        // Observe LiveData
        viewModel.getCommentLiveData().observe(this, comments -> {
            // null indicates as initial value or query error
            // Empty list indicates as query successful but no comment
            if (comments != null)
                adapter.setData(viewModel.getLocalComments());
        });

        // Initial fetch
        viewModel.getComments();

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
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            bottomSheet.setLayoutParams(layoutParams);
        });

        return dialog;
    }

    public void createComment(@NonNull String content) {
        viewModel.createComment(content);
    }

    @NonNull
    public static CommentFragment newInstance(@NonNull Post holder) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_POST_ID, holder.getId());
        args.putString(BUNDLE_POST_AUTHOR_ID, holder.getAuthorId());
        fragment.setArguments(args);
        return fragment;
    }
}
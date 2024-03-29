package com.ogif.kotae.ui.comment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Comment;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.ui.comment.adapter.CommentAdapter;
import com.ogif.kotae.ui.questiondetail.QuestionDetailActivity;
import com.ogif.kotae.utils.model.UserUtils;
import com.ogif.kotae.utils.ui.LazyLoadScrollListener;


public class CommentFragment extends BottomSheetDialogFragment {
    public static final String TAG = "CommentFragment";
    public static final String BUNDLE_POST = "post";

    private RecyclerView recyclerView;
    private ConstraintLayout emptyView;
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

        Post post = args.getParcelable(BUNDLE_POST);
        String userId = UserUtils.getCachedUserId(requireActivity());
        String username = UserUtils.getCachedUsername(requireActivity());

        // Binding stuffs
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet_dialog_comment);
        recyclerView = dialog.findViewById(R.id.recycler_view_bottom_sheet_comment);
        emptyView = dialog.findViewById(R.id.empty_list_bottom_sheet_comment);
        if (emptyView != null) {
            TextView tv = emptyView.findViewById(R.id.tv_empty_list);
            tv.setText(R.string.no_comment);
        }
        sendComment = dialog.findViewById(R.id.btn_comment_send);
        inputComment = dialog.findViewById(R.id.et_comment_input);
        assert recyclerView != null;
        assert sendComment != null;
        assert inputComment != null;

        CommentViewModelFactory commentViewModelFactory = new CommentViewModelFactory(userId, username, post);
        this.viewModel = new ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel.class);

        // Set adapter and LazyLoadScrollListener
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        adapter = new CommentAdapter(requireActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new LazyLoadScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                viewModel.getComments();
            }
        });

        adapter.setOnVoteChangeListener((position, voteView, previous, current) -> {
            Comment holder = (Comment) voteView.getHolder();
            if (holder == null) {
                Log.w(TAG, "Unidentified holder for VoteView, did you forget to setHolder()?");
                return;
            }
            viewModel.updateVote(holder, previous, current);
        });

        // Observe LiveData
        viewModel.getCommentLiveData().observe(this, latestComments -> {
            // null indicates as initial value or query error
            // Empty list indicates as query successful but no comment
            if (latestComments != null) {
                if (viewModel.getImmutableLocalComments().isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    adapter.setItems(viewModel.getImmutableLocalComments());
                }
            }
            inputComment.setText("");
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        QuestionDetailActivity activity = ((QuestionDetailActivity) getContext());
        if (activity != null) {
            activity.refresh();
        }
    }

    public void createComment(@NonNull String content) {
        viewModel.createComment(content);
    }

    @NonNull
    public static CommentFragment newInstance(@NonNull Post parent) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_POST, parent);
        fragment.setArguments(args);
        return fragment;
    }
}
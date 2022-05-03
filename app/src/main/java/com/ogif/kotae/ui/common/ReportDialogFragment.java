package com.ogif.kotae.ui.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Record;

public class ReportDialogFragment extends DialogFragment {
    private static final String BUNDLE_RECORD_ID = "recordId";
    private String recordId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.report);
        builder.setSingleChoiceItems(R.array.report_reasons, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something
            }
        }).setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }

    public static ReportDialogFragment newInstance(String recordId) {
        ReportDialogFragment dialog = new ReportDialogFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_RECORD_ID, recordId);
        dialog.setArguments(args);
        return dialog;
    }
}

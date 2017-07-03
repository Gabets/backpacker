package com.blessedtactics.programs.backpacker.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.blessedtactics.programs.backpacker.PrepareListActivity;
import com.blessedtactics.programs.backpacker.R;

public class DeleteItemDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        final String name = bundle.getString("name");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.title_delete) + name)
                .setNeutralButton(R.string.btn_delete_from_db, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((PrepareListActivity) getActivity()).deleteFromDB();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.btn_delete_from_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PrepareListActivity) getActivity()).deleteFromList();
                        dialog.cancel();
                    }
                })
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
        ;

        return builder.create();
    }
}

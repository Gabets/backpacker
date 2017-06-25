package com.blessedtactics.programs.backpacker.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.blessedtactics.programs.backpacker.R;

public class AddItemDialog extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create items array
        Bundle bundle = getArguments();
        String[] itemsArray = bundle.getStringArray("items");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_choose_item)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setItems(itemsArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TO DO

                    }
                });

        return builder.create();
    }
}

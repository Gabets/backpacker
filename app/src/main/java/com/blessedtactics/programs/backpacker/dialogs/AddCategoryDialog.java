package com.blessedtactics.programs.backpacker.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.blessedtactics.programs.backpacker.App;
import com.blessedtactics.programs.backpacker.PrepareListActivity;
import com.blessedtactics.programs.backpacker.R;

public class AddCategoryDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create categories array
        Bundle bundle = getArguments();
        final String[] categoriesArray = bundle.getStringArray("categories");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_choose_category)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TO DO
                        Log.d(App.LOG_TAG, "int which = " + which);
                        if (categoriesArray != null) {
                            ((PrepareListActivity) getActivity()).addCategory(categoriesArray[which]);
                        }

                    }
                });

        return builder.create();
    }
}

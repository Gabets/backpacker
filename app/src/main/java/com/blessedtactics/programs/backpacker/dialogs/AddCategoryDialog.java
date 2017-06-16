package com.blessedtactics.programs.backpacker.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.blessedtactics.programs.backpacker.R;

public class AddCategoryDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create categories array
        final String[] categoriesArray = {"1", "2", "3"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_choose_category)
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TO DO

                    }
                });




        return builder.create();
    }
}

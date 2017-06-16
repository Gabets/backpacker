package com.blessedtactics.programs.backpacker.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.blessedtactics.programs.backpacker.R;

public class CreateCategoryDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create items array

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.title_input_new_category);




        return builder.create();
    }
}

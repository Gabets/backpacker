package com.blessedtactics.programs.backpacker.dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_add_item, null);

        ExpandableListView elvItems = (ExpandableListView) view.findViewById(R.id.elvItems);
        elvItems.


        ExpandableListView elvCategories = (ExpandableListView) view.findViewById(R.id.elvCategories);

        builder.setView(view);
        return builder.create();
    }
}

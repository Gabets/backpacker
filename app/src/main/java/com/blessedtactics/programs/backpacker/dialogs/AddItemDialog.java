package com.blessedtactics.programs.backpacker.dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.blessedtactics.programs.backpacker.PrepareListActivity;
import com.blessedtactics.programs.backpacker.R;

public class AddItemDialog extends DialogFragment {

    private String mSelectedCategory;
    private String mSelectedItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create items array
        Bundle bundle = getArguments();
        String[] categoriesArray = bundle.getStringArray("categories");
        String[] itemsArray = bundle.getStringArray("items");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_add_item, null);

        final Spinner spCategories = (Spinner) view.findViewById(R.id.sp_DAdd_categories);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoriesArray);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(categoriesAdapter);

        final Spinner spItems = (Spinner) view.findViewById(R.id.sp_DAdd_items);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemsArray);
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItems.setAdapter(itemsAdapter);


        Button btnOk = (Button) view.findViewById(R.id.btn_DAdd_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedCategory = spCategories.getSelectedItem().toString();
                mSelectedItem = spItems.getSelectedItem().toString();

                ((PrepareListActivity) getActivity()).addItem(mSelectedCategory, mSelectedItem);

                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btn_DAdd_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });





        builder.setView(view);
        return builder.create();
    }
}

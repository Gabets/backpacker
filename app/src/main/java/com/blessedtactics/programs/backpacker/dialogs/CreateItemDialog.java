package com.blessedtactics.programs.backpacker.dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blessedtactics.programs.backpacker.PrepareListActivity;
import com.blessedtactics.programs.backpacker.R;

public class CreateItemDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_create, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.title_input_new_item);

        final EditText etName = (EditText) view.findViewById(R.id.etName);

        Button btnOK = (Button) view.findViewById(R.id.btnDCOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText() != null && etName.getText().toString().trim().length() > 0) {
                    ((PrepareListActivity) getActivity()).createItem(etName.getText().toString());
                    dismiss();
                } else {
                    etName.setText("");
                    Toast toast = Toast.makeText(getActivity(), getResources().getText(R.string.toast_empty_name), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnDCCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

}

package com.blessedtactics.programs.backpacker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blessedtactics.programs.backpacker.models.Item;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class PrepareListAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> mItems;
    private int mResource;
    private LayoutInflater mInflater;

    public PrepareListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Item> items) {
        super(context, resource, items);

        mItems = items;
        mResource = resource;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return mItems.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(mResource, parent, false);
        }

        Item item = getItem(position);
        if (item.getType() == 'c') {
            ((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            ((TextView) view).setTypeface(Typeface.DEFAULT);
        }
        ((TextView) view).setText(item.getName());


        return view;
    }

}

package com.blessedtactics.programs.backpacker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blessedtactics.programs.backpacker.R;
import com.blessedtactics.programs.backpacker.models.Item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.Inflater;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class PrepareListAdapter extends ArrayAdapter<Item> {

    private int mResource;
    private LayoutInflater mInflater;

    @ColorInt
    private final int PACKED_COLOR = Color.argb(255, 23, 215, 87);

    private LinkedList<Item> mItems;

    public PrepareListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull LinkedList<Item> items) {
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
        if (item != null) {
            TextView tv = (TextView) view;
            if (item.getType().equalsIgnoreCase("c")) {
                tv.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                tv.setTypeface(Typeface.DEFAULT);
            }
            tv.setText(item.getName());

            if (item.isPacked()) {
                tv.setBackgroundColor(PACKED_COLOR);
            } else {
                tv.setBackgroundColor(Color.WHITE);
            }
        }

        return view;
    }
}

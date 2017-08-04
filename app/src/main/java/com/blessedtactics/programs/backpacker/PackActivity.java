package com.blessedtactics.programs.backpacker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blessedtactics.programs.backpacker.adapters.PrepareListAdapter;
import com.blessedtactics.programs.backpacker.models.Item;
import com.blessedtactics.programs.backpacker.models.ListItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PackActivity extends AppCompatActivity {

    private PrepareListAdapter mAdapter;
    private LinkedList<Item> mItemsList;

    private Realm mRealm;

    private final String CATEGORY_NAME = "category.name";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        mRealm = Realm.getDefaultInstance();

        mItemsList = resultsToList();
        mAdapter = new PrepareListAdapter(this, R.layout.item_prepare_list, mItemsList);

        ListView lvPack = (ListView) findViewById(R.id.lv_pack);
        lvPack.setAdapter(mAdapter);
        lvPack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item clickedItem = (Item) parent.getItemAtPosition(position);
                final String itemName = clickedItem.getName();

                RealmResults<ListItem> results =  mRealm.where(ListItem.class).findAll();
                for (ListItem listItem : results) {
                    for (final Item item : listItem.getItems()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    item.setPacked(!item.isPacked());
                                }
                            });
                            mAdapter.notifyDataSetChanged();
                            return;
                        }

                    }
                }
            }
        });


    }

    private LinkedList<Item> resultsToList() {
        RealmResults<ListItem> results =  mRealm.where(ListItem.class).findAll().sort(CATEGORY_NAME);
        LinkedList<Item> fullList = new LinkedList<>();
        for (ListItem listItem : results) {
            LinkedList<Item> list = new LinkedList<>();

            for (Item item : listItem.getItems()) {
                list.add(item);
            }

            Collections.sort(list, new Comparator<Item>() {
                @Override
                public int compare(Item i1, Item i2) {
                    return i1.getName().compareToIgnoreCase(i2.getName());
                }
            });
            list.add(0, listItem.getCategory());

            fullList.addAll(list);
        }
        return fullList;
    }

    public void pnClickDeselect(View view) {
        RealmResults<ListItem> results =  mRealm.where(ListItem.class).findAll();
        for (ListItem listItem : results) {
            for (Item item : listItem.getItems()) {

            }
        }
    }
}

package com.blessedtactics.programs.backpacker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blessedtactics.programs.backpacker.adapters.PrepareListAdapter;
import com.blessedtactics.programs.backpacker.dialogs.AddCategoryDialog;
import com.blessedtactics.programs.backpacker.dialogs.AddItemDialog;
import com.blessedtactics.programs.backpacker.dialogs.CreateCategoryDialog;
import com.blessedtactics.programs.backpacker.dialogs.CreateItemDialog;
import com.blessedtactics.programs.backpacker.dialogs.DeleteItemDialog;
import com.blessedtactics.programs.backpacker.models.Item;
import com.blessedtactics.programs.backpacker.models.ListItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PrepareListActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private PrepareListAdapter mAdapter;
    private LinkedList<Item> mItemsList;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_list);

        mFragmentManager = getSupportFragmentManager();
        mRealm = Realm.getDefaultInstance();

        mItemsList = resultsToList();
        mAdapter = new PrepareListAdapter(this, R.layout.item_prepare_list, mItemsList);

        ListView lvPrepareList = (ListView) findViewById(R.id.lvPrepareList);
        lvPrepareList.setAdapter(mAdapter);
        lvPrepareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                onClickListItem(item);
            }
        });
    }

    private LinkedList<Item> resultsToList() {
        RealmResults<ListItem> results =  mRealm.where(ListItem.class).findAll().sort("category.name");
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

    public void onClickAddCategory(View view) {
        RealmResults<Item> categories = mRealm.where(Item.class).equalTo("type", "c").findAll().sort("name");
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }

        Bundle bundle = new Bundle();
        bundle.putStringArray("categories", categoryNames);

        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.setArguments(bundle);
        addCategoryDialog.show(mFragmentManager, "add category dialog");
    }

    public void addCategory(final String categoryName) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item categoryItem = realm.where(Item.class).equalTo("name", categoryName).findFirst();

                ListItem listItem = realm.createObject(ListItem.class);
                listItem.setCategory(categoryItem);
            }
        });
        for (int i = 0; i < mItemsList.size(); i++) {
            Item item = mItemsList.get(i);
            if (item.getType().equalsIgnoreCase("c")
                    && item.getName().compareToIgnoreCase(categoryName) > 0) {
                mItemsList.add(i, new Item(categoryName, "c"));
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
        mItemsList.add(new Item(categoryName, "c"));
        mAdapter.notifyDataSetChanged();
    }

    public void onClickAddItem(View view) {
        RealmResults<Item> categories = mRealm.where(Item.class).equalTo("type", "i").findAll().sort("name");
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }

        Bundle bundle = new Bundle();
        bundle.putStringArray("items", categoryNames);

        AddItemDialog addItemDialog = new AddItemDialog();
        addItemDialog.setArguments(bundle);
        addItemDialog.show(mFragmentManager, "add item dialog");
    }

    public void onClickCreateCategory(View view) {
        CreateCategoryDialog createCategoryDialog = new CreateCategoryDialog();
        createCategoryDialog.show(mFragmentManager, "create category");
    }
    public void createCategory(final String categoryName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item = mRealm.createObject(Item.class);
                item.setName(categoryName);
                item.setType("c");
            }
        });
    }

    public void onClickCreateItem(View view) {
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.show(mFragmentManager, "create item");
    }
    public void createItem(final String itemName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item = mRealm.createObject(Item.class);
                item.setName(itemName);
                item.setType("i");
            }
        });
    }

    private void onClickListItem(Item item) {
        final String name = item.getName();
        Log.d(App.LOG_TAG, "onClickListItem item = " + name);

        Bundle bundle = new Bundle();
        bundle.putString("name", name);

        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();
        deleteItemDialog.setArguments(bundle);
        deleteItemDialog.show(mFragmentManager, "delete item");
    }

    public void deleteFromDB(String name) {
        final RealmResults<Item> items = mRealm.where(Item.class).equalTo("name", name).findAll();
        if (items != null) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    items.deleteAllFromRealm();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

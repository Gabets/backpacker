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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PrepareListActivity extends AppCompatActivity {

    private final String CATEGORY_NAME = "category.name";

    private FragmentManager mFragmentManager;
    private PrepareListAdapter mAdapter;
    private LinkedList<Item> mItemsList;

    private Realm mRealm;

    private int mClickedPosition;
    private Item mClickedItem;

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
                mClickedPosition = position;
                mClickedItem = (Item) parent.getItemAtPosition(position);
                onClickListItem();
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
        ArrayList<String> categoriesList = new ArrayList<>();
        for (Item item : mItemsList) {
            if (item.getType().equalsIgnoreCase("c")) {
                categoriesList.add(item.getName());
            }
        }
        String[] categoriesNames = categoriesList.toArray(new String[categoriesList.size()]);

        RealmResults<Item> items = mRealm.where(Item.class).equalTo("type", "i").findAll().sort("name");
        String[] itemsNames = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemsNames[i] = items.get(i).getName();
        }

        Bundle bundle = new Bundle();
        bundle.putStringArray("categories", categoriesNames);
        bundle.putStringArray("items", itemsNames);

        AddItemDialog addItemDialog = new AddItemDialog();
        addItemDialog.setArguments(bundle);
        addItemDialog.show(mFragmentManager, "add item dialog");
    }
    public void addItem(String categoryName, String itemName) {
        Log.d("add items", "category = " + categoryName);
        Log.d("add items", "item = " + itemName);

        //1) add item to list
        for (int i = 0; i < mItemsList.size(); i++) {
            Item item = mItemsList.get(i);
            if (item.getType().equalsIgnoreCase("c") &&
                    item.getName().equalsIgnoreCase(categoryName)) {

                for (int j = i + 1; j < mItemsList.size(); j++) {
                    item = mItemsList.get(j);
                    if (item.getType().equalsIgnoreCase("c") || //found next category
                            item.getName().compareToIgnoreCase(itemName) > 0) {
                        mItemsList.add(j, new Item(itemName, "i"));
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                //if end of list is reached
                mItemsList.add(new Item(itemName, "i"));
                mAdapter.notifyDataSetChanged();
                return;
            }
        }





        //2) add item to the ListItem
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

    private void onClickListItem() {
        Log.d(App.LOG_TAG, "onClickListItem item = " + mClickedItem.getName() + " type = " + mClickedItem.getType() );

        Bundle bundle = new Bundle();
        bundle.putString("name", mClickedItem.getName());

        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();
        deleteItemDialog.setArguments(bundle);
        deleteItemDialog.show(mFragmentManager, "delete item");
    }

    public void deleteFromDB() {
        mItemsList.remove(mClickedPosition);
        mAdapter.notifyDataSetChanged();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Item> items = mRealm.where(Item.class)
                        .equalTo("name", mClickedItem.getName())
                        .equalTo("type", mClickedItem.getType())
                        .findAll();
                items.deleteAllFromRealm();
            }
        });
    }

    public void deleteFromList() {

        if (mClickedItem.getType().equalsIgnoreCase("c")) {
            //if it is a category
            do {
                mItemsList.remove(mClickedPosition);
            } while (mClickedPosition < mItemsList.size() &&
                    !mItemsList.get(mClickedPosition).getType().equalsIgnoreCase("c"));

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ListItem listItem =  mRealm.where(ListItem.class)
                            .equalTo(CATEGORY_NAME, mClickedItem.getName())
                            .findFirst();
                    listItem.deleteFromRealm();
                }
            });

        } else {
            //if it is a item
            mItemsList.remove(mClickedPosition);
            for (int i = mClickedPosition - 1; i >=0; i--) {
                final Item category = mItemsList.get(i);

                if (category.getType().equalsIgnoreCase("c")) {
                    final Item item = mClickedItem;
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ListItem listItem = mRealm.where(ListItem.class)
                                    .equalTo(CATEGORY_NAME, category.getName())
                                    .findFirst();
                            Log.d(App.LOG_TAG, "category = " + listItem.getCategory().getName());

                            listItem.getItems().remove(item);
                        }
                    });
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

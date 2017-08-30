package com.blessedtactics.programs.backpacker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class PrepareListActivity extends AppCompatActivity {

    private final String CATEGORY_NAME = "category.name";

    private FragmentManager mFragmentManager;
    private PrepareListAdapter mAdapter;
    private LinkedList<Item> mItemsList;

    private Realm mRealm;

    private int mClickedPosition;
    private Item mClickedItem;
    private int mQuantity = 0;

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

        for (Item category: mItemsList) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                Toast.makeText(this, getString(R.string.toast_ban_add_double_category_text), Toast.LENGTH_SHORT).show();
                return;
            }
        }

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
                mItemsList.add(i, new Item(categoryName, "c", false));
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
        mItemsList.add(new Item(categoryName, "c", false));
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

        RealmQuery<Item> query = mRealm.where(Item.class);
        query.equalTo("type", "i");

        RealmResults<Item> items = mRealm.where(Item.class)
                .equalTo("type", "i")
                .distinct("name")
                .sort("name");

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
    public void addItem(final String categoryName, final String itemName) {
        //add item to the ListItem
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                ListItem toUpdateLI = mRealm.where(ListItem.class).equalTo(CATEGORY_NAME, categoryName).findFirst();

                RealmResults<Item> results = mRealm.where(Item.class)
                        .equalTo("name", itemName)
                        .findAll();

                for (Item item : results) {
                    if (mQuantity < item.getQuantity()) {
                        mQuantity = item.getQuantity();
                    }
                }

                for (Item item : results) {
                    if (!item.isInList()) {
                        toUpdateLI.getItems().add(item);
                        item.setInList(true);

                        return;
                    }
                }

                mQuantity = mQuantity + 1;
                Item item = realm.createObject(Item.class);
                item.setName(itemName);
                item.setType("i");
                item.setQuantity(mQuantity);


                toUpdateLI.getItems().add(item);
                item.setInList(true);
            }
        });

        //add item to list
        for (int i = 0; i < mItemsList.size(); i++) {
            Item item = mItemsList.get(i);
            if (item.getType().equalsIgnoreCase("c") &&
                    item.getName().equalsIgnoreCase(categoryName)) {

                for (int j = i + 1; j < mItemsList.size(); j++) {
                    item = mItemsList.get(j);
                    if (item.getType().equalsIgnoreCase("c") || //found next category
                            item.getName().compareToIgnoreCase(itemName) > 0) {
                        mItemsList.add(j, new Item(itemName, "i", false, true, mQuantity));
                        mAdapter.notifyDataSetChanged();
                        mQuantity = 0;
                        return;
                    }
                }
                //if end of list is reached
                mItemsList.add(new Item(itemName, "i", false, true, mQuantity));
                mAdapter.notifyDataSetChanged();
                mQuantity = 0;
                return;
            }
        }
    }


    public void onClickCreateCategory(View view) {
        CreateCategoryDialog createCategoryDialog = new CreateCategoryDialog();
        createCategoryDialog.show(mFragmentManager, "create category");
    }
    public void createCategory(final String categoryName) {
        RealmResults<Item> results = mRealm.where(Item.class)
                .equalTo("name", categoryName)
                .findAll();
        if (results.size() != 0) {
            Toast.makeText(this, getString(R.string.toast_ban_create_double_category_text), Toast.LENGTH_SHORT).show();
            return;
        }

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

        RealmResults<Item> results = mRealm.where(Item.class)
                .equalTo("name", itemName)
                .findAll();
        if (results.size() != 0) {
            Toast.makeText(this, getString(R.string.toast_ban_create_double_item_text), Toast.LENGTH_SHORT).show();
            return;
        }

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<Item> results = mRealm.where(Item.class).equalTo("name", itemName).findAll();

                if (results.size() != 0) {
                    return;
                }

                Item item = mRealm.createObject(Item.class);
                item.setName(itemName);
                item.setType("i");
            }
        });
    }

    private void onClickListItem() {
        Log.d(App.LOG_TAG, "onClickListItem item = " + mClickedItem.getName()
                + " quantity = " + mClickedItem.getQuantity());

        Bundle bundle = new Bundle();
        bundle.putString("name", mClickedItem.getName());

        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();
        deleteItemDialog.setArguments(bundle);
        deleteItemDialog.show(mFragmentManager, "delete item");
    }

    public void deleteFromList() {

        if (mClickedItem.getType().equalsIgnoreCase("i")) { //remove Item
            mItemsList.remove(mClickedPosition);            //from list

            for (int i = mClickedPosition - 1; i >=0; i--) {//from List Item in DB
                final Item category = mItemsList.get(i);

                if (category.getType().equalsIgnoreCase("c")) {
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            Item item = mRealm.where(Item.class)
                                    .equalTo("name", mClickedItem.getName())
                                    .equalTo("quantity", mClickedItem.getQuantity())
                                    .findFirst();


                            ListItem listItem = mRealm.where(ListItem.class)
                                    .equalTo(CATEGORY_NAME, category.getName())
                                    .findFirst();
                            Log.d(App.LOG_TAG, "category = " + listItem.getCategory().getName());

                            listItem.getItems().remove(item);
                            item.setInList(false);
                            item.setPacked(false);
                        }
                    });
                    break;
                }
            }
        } else {                                                 //remove List Item
            Item item;
            do {                                                //from list
                mItemsList.remove(mClickedPosition);
                if (mClickedPosition >= mItemsList.size()) {
                    break;
                }
                item = mItemsList.get(mClickedPosition);
            } while (item.getType().equalsIgnoreCase("i"));

            mRealm.executeTransaction(new Realm.Transaction() { //from DB
                @Override
                public void execute(Realm realm) {
                    ListItem listItem =  mRealm.where(ListItem.class)
                            .equalTo(CATEGORY_NAME, mClickedItem.getName())
                            .findFirst();
                    listItem.deleteFromRealm();
                }
            });
        }

        mAdapter.notifyDataSetChanged();
    }

    public void deleteFromDB() {
        deleteFromList();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Item> items = mRealm.where(Item.class)
                        .equalTo("name", mClickedItem.getName())
                        .equalTo("type", mClickedItem.getType())
                        .equalTo("inList", false)
                        .findAll();
                items.deleteAllFromRealm();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

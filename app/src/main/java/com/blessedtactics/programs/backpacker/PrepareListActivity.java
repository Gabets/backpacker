package com.blessedtactics.programs.backpacker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.blessedtactics.programs.backpacker.adapters.PrepareListAdapter;
import com.blessedtactics.programs.backpacker.dialogs.AddCategoryDialog;
import com.blessedtactics.programs.backpacker.dialogs.AddItemDialog;
import com.blessedtactics.programs.backpacker.dialogs.CreateCategoryDialog;
import com.blessedtactics.programs.backpacker.dialogs.CreateItemDialog;
import com.blessedtactics.programs.backpacker.models.Item;

import io.realm.Realm;
import io.realm.RealmResults;

public class PrepareListActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_list);

        mFragmentManager = getSupportFragmentManager();
        mRealm = Realm.getDefaultInstance();

        PrepareListAdapter mAdapter = new PrepareListAdapter(this, R.layout.item_prepare_list, mRealm.where(Item.class).findAll().sort("name"));

        ListView mLvPrepareList = (ListView) findViewById(R.id.lvPrepareList);
        mLvPrepareList.setAdapter(mAdapter);

    }

    public void onClickAddCategory(View view) {
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.show(mFragmentManager, "add category dialog");

    }

    public void onClickAddItem(View view) {
        AddItemDialog addItemDialog = new AddItemDialog();
        addItemDialog.show(mFragmentManager, "add item dialog");
    }

    public void onClickCreateCategory(View view) {
        CreateCategoryDialog createCategoryDialog = new CreateCategoryDialog();
        createCategoryDialog.show(mFragmentManager, "create category");
    }

    public void onClickCreateItem(View view) {
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.show(mFragmentManager, "create item");
    }

    public void createItem(String name) {
        mRealm.beginTransaction();
        Item item = mRealm.createObject(Item.class);
        item.setName(name);
        item.setType("i");
        mRealm.commitTransaction();
    }

    private void removeFromList(String name) {
        mRealm.beginTransaction();
        RealmResults<Item> items = mRealm.where(Item.class).equalTo("name", name).findAll();
        if (!items.isEmpty()) {
            for (int i = items.size() -1; i >= 0; i--) {
                items.get(i).deleteFromRealm();
            }
        }
        mRealm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

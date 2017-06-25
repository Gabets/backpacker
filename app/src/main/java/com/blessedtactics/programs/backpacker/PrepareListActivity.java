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

        PrepareListAdapter mAdapter = new PrepareListAdapter(this, R.layout.item_prepare_list, mRealm.where(Item.class)
                .findAll().sort("name"));

        ListView mLvPrepareList = (ListView) findViewById(R.id.lvPrepareList);
        mLvPrepareList.setAdapter(mAdapter);
        mLvPrepareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                onClickListItem(item);
            }
        });

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

    private void onClickListItem(Item item) {
        final String name = item.getName();
        Log.d(App.LOG_TAG, "onClickListItem item = " + name);

        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();
        deleteItemDialog.show(mFragmentManager, "delete item");

//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                RealmResults<Item> items = mRealm.where(Item.class).equalTo("name", name).findAll();
//                if (!items.isEmpty()) {
//                    items.deleteAllFromRealm();
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

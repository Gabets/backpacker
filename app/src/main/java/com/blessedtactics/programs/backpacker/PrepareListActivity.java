package com.blessedtactics.programs.backpacker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blessedtactics.programs.backpacker.adapters.PrepareListAdapter;
import com.blessedtactics.programs.backpacker.data.ItemContract.ItemsDbColumns;
import com.blessedtactics.programs.backpacker.data.ItemsDbHelper;
import com.blessedtactics.programs.backpacker.dialogs.AddCategoryDialog;
import com.blessedtactics.programs.backpacker.dialogs.AddItemDialog;
import com.blessedtactics.programs.backpacker.dialogs.CreateCategoryDialog;
import com.blessedtactics.programs.backpacker.dialogs.CreateItemDialog;
import com.blessedtactics.programs.backpacker.models.Item;

import java.util.ArrayList;

public class PrepareListActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private ListView mLvPrepareList;
    private PrepareListAdapter mAdapter;

    private ArrayList<Item> items;

    private ItemsDbHelper mItemsHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_list);

        mFragmentManager = getSupportFragmentManager();

        mItemsHelper = new ItemsDbHelper(this);
        items = new ArrayList<>();

        getDBInfo();
        mAdapter = new PrepareListAdapter(this, R.layout.item_prepare_list, items);

//        mAdapter = new ArrayAdapter<>(this, R.layout.item_prepare_list, items);
        mLvPrepareList = (ListView) findViewById(R.id.lvPrepareList);
        mLvPrepareList.setAdapter(mAdapter);

    }



    private void getDBInfo() {
        if (!items.isEmpty()) {
            items.clear();
        }
        mDatabase = mItemsHelper.getReadableDatabase();
        String[] projection = { ItemsDbColumns._ID,
                ItemsDbColumns.COLUMN_NAME,
                ItemsDbColumns.COLUMN_TYPE };
        Cursor cursor = mDatabase.query(ItemsDbColumns.TABLE_NAME, projection,
                null, null, null, null, null);

        int idColumnIndex = cursor.getColumnIndex(ItemsDbColumns._ID);
        int nameColumnIndex = cursor.getColumnIndex(ItemsDbColumns.COLUMN_NAME);
        int typeColumnIndex = cursor.getColumnIndex(ItemsDbColumns.COLUMN_TYPE);

        while (cursor.moveToNext()) {
            int currentID = cursor.getInt(idColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            char currentType = cursor.getString(typeColumnIndex).charAt(0);

            items.add(new Item(currentName, currentType));
        }

        cursor.close();
        mDatabase.close();
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

    public void addItem(String name) {
        mDatabase = mItemsHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemsDbColumns.COLUMN_NAME, name);
        values.put(ItemsDbColumns.COLUMN_TYPE, "i");

        mDatabase.insert(ItemsDbColumns.TABLE_NAME, null, values);
        mDatabase.close();
        getDBInfo();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

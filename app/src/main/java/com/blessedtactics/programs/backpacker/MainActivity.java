package com.blessedtactics.programs.backpacker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blessedtactics.programs.backpacker.models.Item;
import com.blessedtactics.programs.backpacker.models.ListItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        firstRunCheck();
    }

    public void onClickPrepareList(View view) {
        startActivity(new Intent(this, PrepareListActivity.class));
    }

    public void onClickPack(View view) {
        startActivity(new Intent(this, PackActivity.class));
    }

    public void onClickInfo(View view) {
        startActivity(new Intent(this, InfoActivity.class));
    }

    private void firstRunCheck() {
        RealmResults<Item> results = mRealm.where(Item.class).findAll();
        Log.d(App.LOG_TAG, "results.size = " + results.size());

        if (results.size() == 0) {
            //create default DB and list
            String[] clothes = getResources().getStringArray(R.array.clothing);
            createListItems(clothes);

            String[] dishes = getResources().getStringArray(R.array.dishes);
            createListItems(dishes);

            String[] forCamps = getResources().getStringArray(R.array.for_camping);
            createListItems(forCamps);

            String[] forWashes = getResources().getStringArray(R.array.for_washing);
            createListItems(forWashes);

            String[] foods = getResources().getStringArray(R.array.food);
            createListItems(foods);

            String[] forFunny = getResources().getStringArray(R.array.fun);
            createListItems(forFunny);

            String[] instruments = getResources().getStringArray(R.array.instruments);
            createListItems(instruments);

            String[] others = getResources().getStringArray(R.array.others);
            createListItems(others);
        }
    }

    private void createListItems(final String[] itemsArray) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ListItem listItem = realm.createObject(ListItem.class);

                Item categoryItem = realm.createObject(Item.class);
                categoryItem.setName(itemsArray[0]);
                categoryItem.setType("c");
                categoryItem.setPacked(false);

                listItem.setCategory(categoryItem);

                RealmList<Item> items = new RealmList<>();
                for (int i = 1; i < itemsArray.length; i++) {
                    Item item = realm.createObject(Item.class);
                    item.setName(itemsArray[i]);
                    item.setType("i");
                    item.setInList(true);
                    item.setPacked(false);
                    items.add(item);
                }
                listItem.setItems(items);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

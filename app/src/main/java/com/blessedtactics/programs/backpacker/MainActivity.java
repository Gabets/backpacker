package com.blessedtactics.programs.backpacker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blessedtactics.programs.backpacker.models.Item;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Item> results = realm.where(Item.class).findAll();
        Log.d(App.LOG_TAG, "results.size = " + results.size());
        if (results.size() == 0) {
            // create default DB
            final String[] categories = getResources().getStringArray(R.array.categories);
            final String[] items = getResources().getStringArray(R.array.items);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    for (String categoryName : categories) {
                        Item item = realm.createObject(Item.class);
                        item.setName(categoryName);
                        item.setType("c");
                    }
                    for (String itemName : items) {
                        Item item = realm.createObject(Item.class);
                        item.setName(itemName);
                        item.setType("i");
                    }
                }
            });

        }
        realm.close();
    }
}

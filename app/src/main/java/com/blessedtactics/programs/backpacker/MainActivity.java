package com.blessedtactics.programs.backpacker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
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
}

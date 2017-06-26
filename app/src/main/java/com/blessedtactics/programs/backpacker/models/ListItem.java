package com.blessedtactics.programs.backpacker.models;


import io.realm.RealmList;
import io.realm.RealmObject;

public class ListItem extends RealmObject {

    private Item category;
    private RealmList<Item> items;

    public Item getCategory() {
        return category;
    }

    public void setCategory(Item category) {
        this.category = category;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }
}

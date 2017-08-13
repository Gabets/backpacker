package com.blessedtactics.programs.backpacker.models;


import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Item  extends RealmObject {

    public Item() {
    }

    public Item(String name, String type, boolean packed, boolean inList) {
        this.name = name;
        this.type = type;
        this.packed = packed;
        this.inList = inList;
    }

    @Required
    private String name;
    private String type;
    private boolean inList;
    private boolean packed;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInList() {
        return inList;
    }

    public void setInList(boolean inList) {
        this.inList = inList;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }
}

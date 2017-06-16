package com.blessedtactics.programs.backpacker.models;


public class Item {
    private String mName;
    private char mType;

    public Item() {}

    public Item(String name, char type) {
        mName = name;
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public char getType() {
        return mType;
    }

}

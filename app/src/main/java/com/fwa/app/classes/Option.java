package com.fwa.app.classes;

public class Option {

    private String defaultShoppingList;

    public Option(){}

    public Option(String list){
        this.defaultShoppingList = list;
    }

    public String getDefaultShoppingList() {
        return defaultShoppingList;
    }
}

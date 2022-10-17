package com.fwa.app.classes;

public class ShoppingList {
    public String list_name;

    public ShoppingList(){}
    public ShoppingList(String name){
        this.list_name = name;
    }

    public String getList_name(){return this.list_name;}
}

package com.fwa.app.classes;

import com.google.firebase.database.Exclude;

public class Product {

    @Exclude
    private String key;
    private String name, barcode, company, storage;//, productAmount;
    private int amount;


    public Product(){}
    public Product(String code, String name, String company, int amount, String storage){

        this.barcode = code;
        this.company = company;
        this.name = name;
        this.amount = amount;
        this.storage = storage;

    }

    public String getBarcode(){ return  this.barcode; }
    public String getCompany(){ return this.company; }
    public String getName(){ return this.name; }
    public int getAmount(){ return this.amount; }
    public String getStorage(){ return this.storage; }
    public String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.key = key;
    }

}

package com.fwa.app.classes;

//import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Product {

    @Exclude // Firebase
    @com.google.firebase.database.Exclude // FireStore
    private String key;
    private String name, barcode, company; //, storage;//, productAmount;
    private String amount;
    private int quantity;
    private List<String> storage;


    public Product(){}
    public Product(String code, String name, String company, String amount, int quantity, List<String> storage){

        this.barcode = code;
        this.company = company;
        this.name = name;
        this.amount = amount;
        this.quantity = quantity;
        this.storage = storage;

    }

    public String getBarcode(){ return  this.barcode; }
    public String getCompany(){ return this.company; }
    public String getName(){ return this.name; }

    public String getAmount(){ return this.amount; }

    public int getQuantity() { return this.quantity; }

    public List<String> getStorage(){ return this.storage; }

    @Exclude // Firebase
    @com.google.firebase.database.Exclude // FireStore
    public String getKey()
    {
        return key;
    }

    @Exclude
    @com.google.firebase.database.Exclude // FireStore
    public void setKey(String key)
    {
        this.key = key;
    }

}

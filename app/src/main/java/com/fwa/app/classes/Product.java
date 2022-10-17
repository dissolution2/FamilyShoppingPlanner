package com.fwa.app.classes;

public class Product {

    public String name, barcode, company, storage;//, productAmount;
    public int amount;


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

}

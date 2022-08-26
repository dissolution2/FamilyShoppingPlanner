package com.fwa.app.authapp;

public class Product {

    public String productName, barcode;//, productAmount;
    public int productAmount;
    public Product(){}

    public Product(String code, String name, int amount){

        this.barcode = code;
        this.productName = name;
        this.productAmount = amount;

    }

    public String getBarcode(){ return  this.barcode; }
    public String getProductName(){ return this.productName; }
    public int getProductAmount(){ return this.productAmount; }

}

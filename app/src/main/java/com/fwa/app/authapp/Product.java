package com.fwa.app.authapp;

public class Product {

    public String productName, barcode;
    public int productAmount = 0;

    public Product(){}

    public Product(String code, String name, int amount){
        this.barcode = code;
        this.productName = name;
        this.productAmount = amount;

    }


}

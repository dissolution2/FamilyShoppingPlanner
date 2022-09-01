package com.fwa.app.familyshoppingplanner;

public class Product {

    public String productName, barcode, company;//, productAmount;
    public int productAmount;
    public Product(){}

    //public Product(String code){}

    public Product(String code, String name, int amount){

        this.barcode = code;
        this.company = "Empty";
        this.productName = name;
        this.productAmount = amount;

    }

    public Product(String code,String company, String name, int amount){

        this.barcode = code;
        this.company = company;
        this.productName = name;
        this.productAmount = amount;

    }

    public String getBarcode(){ return  this.barcode; }
    public String getProductCompany(){ return this.company; }
    public String getProductName(){ return this.productName; }
    public int getProductAmount(){ return this.productAmount; }

}

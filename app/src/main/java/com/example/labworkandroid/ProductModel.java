package com.example.labworkandroid;

public class ProductModel {
    public String name;
    public String specifications;
    public String brand;
    public int imageRes;

    public ProductModel(String brand, String name, String specifications, int imageRes) {
        this.name = name;
        this.specifications = specifications;
        this.brand = brand;
        this.imageRes = imageRes;
    }
}

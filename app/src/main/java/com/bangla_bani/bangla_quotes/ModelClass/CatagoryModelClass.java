package com.bangla_bani.bangla_quotes.ModelClass;

public class CatagoryModelClass {
    private int image;
    private String catagory;
    private String banglaCatagoryName;

    public CatagoryModelClass(int image, String catagory, String banglaCatagoryName) {
        this.image = image;
        this.catagory = catagory;
        this.banglaCatagoryName = banglaCatagoryName;
    }

    public int getImage() {
        return image;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getBanglaCatagoryName() {
        return banglaCatagoryName;
    }
}

package com.bangla_bani.bangla_quotes.ModelClass;

public class AuthorModelClass {
    private int image;
    private String name, lifeDetails;

    public AuthorModelClass(int image, String name, String lifeDetails) {
        this.image = image;
        this.name = name;
        this.lifeDetails = lifeDetails;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLifeDetails() {
        return lifeDetails;
    }
}

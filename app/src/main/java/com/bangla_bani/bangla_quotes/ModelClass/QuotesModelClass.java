package com.bangla_bani.bangla_quotes.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

public class QuotesModelClass implements Parcelable {
    private String id, quotes, catagory, favourite;

    public QuotesModelClass(String id, String quotes, String catagory, String favourite) {
        this.id = id;
        this.quotes = quotes;
        this.catagory = catagory;
        this.favourite = favourite;
    }

    protected QuotesModelClass(Parcel in) {
        id = in.readString();
        quotes = in.readString();
        catagory = in.readString();
        favourite = in.readString();
    }

    public static final Creator<QuotesModelClass> CREATOR = new Creator<QuotesModelClass>() {
        @Override
        public QuotesModelClass createFromParcel(Parcel in) {
            return new QuotesModelClass(in);
        }

        @Override
        public QuotesModelClass[] newArray(int size) {
            return new QuotesModelClass[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getQuotes() {
        return quotes;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getFavourite() {
        return favourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(quotes);
        dest.writeString(catagory);
        dest.writeString(favourite);
    }
}

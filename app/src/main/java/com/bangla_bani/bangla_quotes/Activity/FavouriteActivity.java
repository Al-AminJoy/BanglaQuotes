package com.bangla_bani.bangla_quotes.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangla_bani.bangla_quotes.Interface.ClickValuePass;
import com.bangla_bani.bangla_quotes.ModelClass.QuotesModelClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.bangla_bani.bangla_quotes.Adapter.FavouriteAdapter;
import com.bangla_bani.bangla_quotes.Helper.ConstantVariable;
import com.bangla_bani.bangla_quotes.Helper.DatabaseHelper;
import com.bangla_bani.bangla_quotes.Helper.SharedPref;
import com.bangla_bani.bangla_quotes.Interface.NoMessageShowListener;
import com.trustedoffers.banglaquotes.R;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity implements NoMessageShowListener, ClickValuePass {
    //RecyclerView
    private RecyclerView recyclerView;
    //SQL Database
    private DatabaseHelper databaseHelper;

    private ArrayList<QuotesModelClass> datalist = new ArrayList<>();
    private FavouriteAdapter adapter;
    private NoMessageShowListener noMessageShowListener;
    private TextView tvNoFavMessage;
    //Toolbar
    private ImageView ivBack;
    private TextView tvTitle;
    //For Interface
    private ClickValuePass clickValuePass;
    //Ad
    private InterstitialAd interstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        //Initilizing Id
        idFinder();
        //Toolbar
        toolbar();
        //Banner Ad
        MobileAds.initialize(this, String.valueOf(R.string.admob_ad_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //InterstitialAd Implimentation
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        clickValuePass = (ClickValuePass) this;
        //NoMessage Show Interface
        noMessageShowListener = this;
        databaseHelper = new DatabaseHelper(this);
        //recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //ReadingDataFromDatabase
        readData();


    }

    private void idFinder() {
        recyclerView = findViewById(R.id.rvFavId);
        tvNoFavMessage = findViewById(R.id.tvFavNoMessageId);
        ivBack = findViewById(R.id.ivOtherToolbarBackIconId);
        adView = findViewById(R.id.avBannerFavId);
        tvTitle = findViewById(R.id.tvOtherToolbarTitleId);
    }

    private void toolbar() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdBack();
            }
        });
        tvTitle.setText(R.string.favourite);
    }

    private void showAdBack() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        int clickCount = sharedPreferences.getInt(String.valueOf(SharedPref.count), 0);
        clickCount = clickCount + 1;
        int constantClick = ConstantVariable.AdPerClick;
        if (clickCount == constantClick) {
            if (interstitialAd.isLoaded()) {
                setSharedPref(0);
                interstitialAd.show();

                final int finalClickCount = clickCount;
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent intent = new Intent(FavouriteActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                setSharedPref(0);
                Intent intent = new Intent(FavouriteActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            setSharedPref(clickCount);
            Intent intent = new Intent(FavouriteActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void readData() {
        Cursor cursor = databaseHelper.readData();
        if (cursor.getCount() == 0) {
            return;
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String message = cursor.getString(1);
                String catagory = cursor.getString(2);
                String favourite = cursor.getString(3);
                if (favourite.equals("true")) {
                    QuotesModelClass modelClass = new QuotesModelClass(id, message, catagory, favourite);
                    datalist.add(modelClass);
                }
            }
            adapter = new FavouriteAdapter(getApplicationContext(), datalist, clickValuePass, noMessageShowListener);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (datalist.size() == 0) {
                tvNoFavMessage.setVisibility(View.VISIBLE);
            } else {
                tvNoFavMessage.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void noMessage(int num) {
        if (num == 0) {
            tvNoFavMessage.setVisibility(View.VISIBLE);
        } else {
            tvNoFavMessage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavouriteActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void ShowAd() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        int clickCount = sharedPreferences.getInt(String.valueOf(SharedPref.count), 0);
        clickCount = clickCount + 1;
        int constantClick = ConstantVariable.AdPerClick;
        if (clickCount == constantClick) {
            if (interstitialAd.isLoaded()) {
                setSharedPref(0);
                interstitialAd.show();

                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {

                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });
            } else {
                setSharedPref(0);
            }
        } else {
            setSharedPref(clickCount);
        }
    }

    private void setSharedPref(int clickCount) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(SharedPref.count), clickCount);
        editor.apply();
    }
}

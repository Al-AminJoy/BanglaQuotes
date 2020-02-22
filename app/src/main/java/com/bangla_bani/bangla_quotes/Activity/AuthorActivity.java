package com.bangla_bani.bangla_quotes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangla_bani.bangla_quotes.ModelClass.AuthorModelClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.bangla_bani.bangla_quotes.Helper.AuthorData;
import com.bangla_bani.bangla_quotes.Helper.ConstantVariable;
import com.bangla_bani.bangla_quotes.Helper.SharedPref;
import com.trustedoffers.banglaquotes.R;

import java.util.ArrayList;

public class AuthorActivity extends AppCompatActivity {
    //Toolbar
    private ImageView ivBack;
    private TextView tvTitleBarName;
    private ImageView ivAuthor;

    private String catagoryName, banglaCatagoryName;
    private TextView tvAuthor, tvAuthorDetails;

    private ArrayList<AuthorModelClass> listItem = new ArrayList<>();
    //Ad
    private InterstitialAd interstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        //Get Catagory Name From Intent
        catagoryName = getIntent().getStringExtra("Catagory");
        banglaCatagoryName = getIntent().getStringExtra("BanglaCatagory");
        //Initialization Id
        idFinder();
        //Banner Ad
        MobileAds.initialize(this, String.valueOf(R.string.admob_ad_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //InterstitialAd Implimentation
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        //Toolbar
        toolbar();
        //Getting Author Data
        AuthorData data=new AuthorData();
        listItem=data.authorList;
        for (AuthorModelClass modelClass : listItem) {
            if (catagoryName.contains(modelClass.getName())) {
                ivAuthor.setImageResource(modelClass.getImage());
                tvAuthor.setText(banglaCatagoryName);
                tvAuthorDetails.setText(modelClass.getLifeDetails());
            }
        }
    }

    private void toolbar() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdBack();
            }
        });
        tvTitleBarName.setText(banglaCatagoryName);

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
                        finish();
                    }
                });
            } else {
                setSharedPref(0);
                finish();
            }
        } else {
            setSharedPref(clickCount);
            finish();
        }
    }

    private void idFinder() {
        ivBack = findViewById(R.id.ivOtherToolbarBackIconId);
        ivAuthor = findViewById(R.id.ivAuthorAuthorImgId);
        tvAuthor = findViewById(R.id.tvAuthorNameId);
        tvAuthorDetails = findViewById(R.id.tvAuthorDetailsId);
        tvTitleBarName = findViewById(R.id.tvOtherToolbarTitleId);
        adView = findViewById(R.id.avBannerAuthorId);
    }

    private void setSharedPref(int clickCount) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(SharedPref.count), clickCount);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        showAdBack();
    }
}

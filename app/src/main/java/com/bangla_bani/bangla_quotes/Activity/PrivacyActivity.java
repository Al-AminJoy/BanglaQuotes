package com.bangla_bani.bangla_quotes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.bangla_bani.bangla_quotes.Helper.ConstantVariable;
import com.bangla_bani.bangla_quotes.Helper.SharedPref;
import com.trustedoffers.banglaquotes.R;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {
    //Ad
    private InterstitialAd interstitialAd;
    private AdView adView;
    //Toolbar
    private ImageView ivBack;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        //Initializing Id
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

        //WebView Load Privacy Policy
        WebView webView = findViewById(R.id.web_view_id);
        webView.getSettings().getJavaScriptEnabled();
        webView.loadUrl("https://www.privacypolicygenerator.info/live.php?token=iRPQ6DqRLn0eK87vkKsxkMZUldRQTGB7");
    }

    private void toolbar() {
        ivBack.setOnClickListener(this);
        tvToolbarTitle.setText(R.string.privacy);

    }

    private void idFinder() {
        adView = findViewById(R.id.avBannerPrivacyId);
        ivBack = findViewById(R.id.ivOtherToolbarBackIconId);
        tvToolbarTitle = findViewById(R.id.tvOtherToolbarTitleId);

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
                        Intent intent = new Intent(PrivacyActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                setSharedPref(0);
                Intent intent = new Intent(PrivacyActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            setSharedPref(clickCount);
            Intent intent = new Intent(PrivacyActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOtherToolbarBackIconId) {
            showAdBack();
        }
    }
}

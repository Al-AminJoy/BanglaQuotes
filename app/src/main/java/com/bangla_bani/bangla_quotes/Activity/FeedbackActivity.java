package com.bangla_bani.bangla_quotes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class FeedbackActivity extends AppCompatActivity {
    private EditText feedbackTxt;
    private Button sendFeedbackBn;
    private TextView tvTitle;
    private ImageView ivToolbarBack;
    private InterstitialAd interstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //Variable Initiation
        idFinder();
        //Banner Ad
        MobileAds.initialize(this, String.valueOf(R.string.admob_ad_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //InterstitialAd Implimentation
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.industrial_ad_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        //Send Feedback : Button Handler : Toolbar
        toolBar();
        //Feedback
        sendFeedbackBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    private void idFinder() {
        feedbackTxt = findViewById(R.id.feedback_edtxt_id);
        sendFeedbackBn = findViewById(R.id.send_feedback_bn_id);
        tvTitle = findViewById(R.id.tvOtherToolbarTitleId);
        ivToolbarBack = findViewById(R.id.ivOtherToolbarBackIconId);
        adView = findViewById(R.id.avBannerFeedbackId);
    }

    /**
     * ToolBar Handler
     */
    private void toolBar() {
        tvTitle.setText(getResources().getString(R.string.feedback_title));
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdBack();
            }
        });
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
                        Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            } else {
                setSharedPref(0);

                Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }


        } else {
            setSharedPref(clickCount);
            Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
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

    /**
     * Feedback Method : Send Using Email
     */
    private void send() {
        String receiver = "kamrul.jaman26@gmail.com";
        String[] recevername = receiver.split(",");
        String subject = "Bangla Love SMS";
        String message = feedbackTxt.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recevername);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose App For Send"));
    }

    @Override
    public void onBackPressed() {
        showAdBack();
    }
}

package com.bangla_bani.bangla_quotes.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangla_bani.bangla_quotes.Interface.ClickValuePass;
import com.bangla_bani.bangla_quotes.ModelClass.CatagoryModelClass;
import com.bangla_bani.bangla_quotes.ModelClass.QuotesModelClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.bangla_bani.bangla_quotes.APIS.API;
import com.bangla_bani.bangla_quotes.Adapter.CatagoryNameAdapter;
import com.bangla_bani.bangla_quotes.Adapter.SwipAdapter;
import com.bangla_bani.bangla_quotes.Helper.CatagoryData;
import com.bangla_bani.bangla_quotes.Helper.ConstantVariable;
import com.bangla_bani.bangla_quotes.Helper.DatabaseHelper;
import com.bangla_bani.bangla_quotes.Helper.SharedPref;
import com.trustedoffers.banglaquotes.R;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity implements ClickValuePass, View.OnClickListener {
    //RecyclerView
    private RecyclerView recyclerViewCatagory;
    //SQL Database
    private DatabaseHelper databaseHelper;

    private ArrayList<QuotesModelClass> datalist = new ArrayList<>();
    private ArrayList<CatagoryModelClass> catagoryList = new ArrayList<>();
    private CatagoryNameAdapter catagoryNameAdapter;
    //For Swip Quotes
    private ViewPager viewPager;
    private SwipAdapter swipAdapter;
    private LinearLayout dotsLayout;
    int customPosition = 0;
    //Search Layout + Toolbar Item
    private ImageView ivSearchIcon;
    private CardView cvSearchField;
    private EditText etSearch;
    private RelativeLayout rlToolbarItems;
    private ImageView ivToolbarBackIcon, ivToolbarFavIcon;
    //Navigation Drawer
    private ImageView ivToolbarMenuIcon;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout linRate, linShare, linFeedback, linFB, linPrivacy;
    private ActionBarDrawerToggle mToggle;
    //For Ad
    private InterstitialAd interstitialAd;
    private AdView adView;
    //For Interface
    private ClickValuePass clickValuePass;
    //WebView For Like FB
    private WebView wvFB;
    private String url = API.fbPageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
        clickValuePass = (ClickValuePass) this;
        //Navigation Drawer
        drawerOperation();
        //Search Catagory
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        //SQL Database
        databaseHelper = new DatabaseHelper(this);
        //RecyclerView For Catagory
        recyclerViewOperation();
        //Toolbar
        toolbar();
        //Read Data From Database
        readData();
        //For Swip Quotes
        swipAdapter = new SwipAdapter(datalist, this, clickValuePass);
        viewPager.setAdapter(swipAdapter);
        getDots(customPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (true) {

                    getDots(position++);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void toolbar() {
        ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvSearchField.setVisibility(View.VISIBLE);
                rlToolbarItems.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                dotsLayout.setVisibility(View.GONE);

            }
        });
        ivToolbarFavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdFav();
            }
        });
        ivToolbarBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvSearchField.setVisibility(View.INVISIBLE);
                rlToolbarItems.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                dotsLayout.setVisibility(View.VISIBLE);
                etSearch.setText("");
            }
        });
    }

    private void recyclerViewOperation() {
        recyclerViewCatagory.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewCatagory.setLayoutManager(gridLayoutManager);
        createCatagoryData();
        catagoryNameAdapter = new CatagoryNameAdapter(this, catagoryList, clickValuePass);
        recyclerViewCatagory.setAdapter(catagoryNameAdapter);
        catagoryNameAdapter.notifyDataSetChanged();
    }

    private void setSharedPref(int clickCount) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.AppPra, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(String.valueOf(SharedPref.count), clickCount);
        editor.apply();
    }

    private void showAdFav() {
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
                        startActivity(new Intent(HomeActivity.this, FavouriteActivity.class));
                        finish();
                    }
                });
            } else {
                setSharedPref(0);
                Intent intent = new Intent(HomeActivity.this, FavouriteActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            setSharedPref(clickCount);
            Intent intent = new Intent(HomeActivity.this, FavouriteActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void filter(String toString) {
        ArrayList<CatagoryModelClass> data = new ArrayList<>();

        for (CatagoryModelClass model : catagoryList) {
            if (model.getCatagory().toLowerCase().contains(toString.toLowerCase()) || model.getBanglaCatagoryName().contains(toString)) {
                data.add(model);
                catagoryNameAdapter.searchResult(data);
            }
        }
    }

    private void createCatagoryData() {
        CatagoryData data=new CatagoryData();
        catagoryList=data.listItem;

    }

    private void drawerOperation() {
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ivToolbarMenuIcon.setOnClickListener(this);
        linFeedback.setOnClickListener(this);
        linRate.setOnClickListener(this);
        linShare.setOnClickListener(this);
        linPrivacy.setOnClickListener(this);
        linFB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.navDrawerRateId) {
            rateApp();
        } else if (v.getId() == R.id.navDrawerShareId) {
            //QR generator
            Bitmap myBitmap = QRCode.from("https://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName()).bitmap();
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            View shareview = getLayoutInflater().inflate(R.layout.qr_layout, null);
            ImageView qrimg = (ImageView) shareview.findViewById(R.id.qrimg);
            Button sharebutton = (Button) shareview.findViewById(R.id.sharebutton);
            qrimg.setImageBitmap(myBitmap);
            builder.setView(shareview);
            builder.create().show();
            sharebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                        String sAux = "\nHey, I'm using this app." + R.string.app_name + " is awesome. Give it a try.\n\n";
                        sAux = sAux + "https://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName() + " \n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                }
            });
        } else if (v.getId() == R.id.navDrawerLikeFBId) {
            wvFB.setVisibility(View.VISIBLE);
            wvFB.getSettings().setJavaScriptEnabled(true);
            wvFB.loadUrl(url);
        } else if (v.getId() == R.id.navDrawerFeedbackId) {
            showAdFeedback();
        } else if (v.getId() == R.id.navDrawerPrivacyId) {
            showAdPrivacy();
        } else if (v.getId() == R.id.ivHomeToolbarMenuIconId) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.tvDialogYesId) {
            finish();
        }

    }

    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getBaseContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName())));
        }

    }

    private void showAdPrivacy() {
        drawerLayout.closeDrawer(GravityCompat.START);
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
                        Intent intent = new Intent(HomeActivity.this, PrivacyActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                setSharedPref(0);

                Intent intent = new Intent(HomeActivity.this, PrivacyActivity.class);
                startActivity(intent);
                finish();
            }


        } else {
            setSharedPref(clickCount);
            Intent intent = new Intent(HomeActivity.this, PrivacyActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showAdFeedback() {
        drawerLayout.closeDrawer(GravityCompat.START);
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
                        Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                setSharedPref(0);

                Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
            }


        } else {
            setSharedPref(clickCount);
            Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
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
                String quotes = cursor.getString(1);
                String catagory = cursor.getString(2);
                String favourite = cursor.getString(3);
                QuotesModelClass modelClass = new QuotesModelClass(id, quotes, catagory, favourite);
                Collections.shuffle(datalist);
                datalist.add(modelClass);
            }

        }
    }

    private void idFinder() {
        recyclerViewCatagory = findViewById(R.id.rvHomeCatagory);
        viewPager = findViewById(R.id.vpHome);
        dotsLayout = findViewById(R.id.linLayHomeDotsId);
        ivSearchIcon = findViewById(R.id.ivHomeSearchIconId);
        cvSearchField = findViewById(R.id.cvHomeToolbarId);
        etSearch = findViewById(R.id.etHomeToolbarSearch);
        rlToolbarItems = findViewById(R.id.rlHomeToolbarItemId);
        ivToolbarBackIcon = findViewById(R.id.ivHomeToolbarBackIconId);
        ivToolbarFavIcon = findViewById(R.id.ivHomeToolbarFavIconId);
        drawerLayout = findViewById(R.id.homeDrawerId);
        navigationView = findViewById(R.id.HomeNavId);
        linRate = findViewById(R.id.navDrawerRateId);
        linShare = findViewById(R.id.navDrawerShareId);
        linFeedback = findViewById(R.id.navDrawerFeedbackId);
        linFB = findViewById(R.id.navDrawerLikeFBId);
        linPrivacy = findViewById(R.id.navDrawerPrivacyId);
        ivToolbarMenuIcon = findViewById(R.id.ivHomeToolbarMenuIconId);
        adView = findViewById(R.id.avBannerHomeId);
        wvFB = findViewById(R.id.wvHomeFB);

    }

    private void getDots(int currentPos) {
        if (dotsLayout.getChildCount() > 0) {
            dotsLayout.removeAllViews();
        }
        ImageView dots[] = new ImageView[5];
        for (int i = 0; i < 5; i++) {
            dots[i] = new ImageView(this);
            if (i == currentPos) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.tab_selected));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.tab_default));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 0, 4, 0);
            dotsLayout.addView(dots[i], layoutParams);

        }
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            wvFB.setVisibility(View.INVISIBLE);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (cvSearchField.getVisibility() == View.INVISIBLE) {
                showDialog();
            } else {
                etSearch.setText("");
                cvSearchField.setVisibility(View.INVISIBLE);
                rlToolbarItems.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                dotsLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    private void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.alert_dialog_layout, null);
        TextView tvYes = view.findViewById(R.id.tvDialogYesId);
        TextView tvNo = view.findViewById(R.id.tvDialogNoId);
        TextView tvRate = view.findViewById(R.id.tvDialogRateId);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view).setCancelable(false).create();
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });
        tvYes.setOnClickListener(this);
        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
                alertDialog.cancel();
            }
        });
        alertDialog.show();
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
}

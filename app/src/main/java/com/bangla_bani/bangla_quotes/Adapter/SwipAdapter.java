package com.bangla_bani.bangla_quotes.Adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bangla_bani.bangla_quotes.Interface.ClickValuePass;
import com.bangla_bani.bangla_quotes.ModelClass.QuotesModelClass;
import com.bangla_bani.bangla_quotes.Helper.DatabaseHelper;
import com.trustedoffers.banglaquotes.R;

import java.util.ArrayList;

public class SwipAdapter extends PagerAdapter {
    private ArrayList<QuotesModelClass> datalist;
    private Context context;
    private LayoutInflater layoutInflater;
    //SQL Database
    private DatabaseHelper databaseHelper;
    //Interface
    private ClickValuePass clickValuePass;

    public SwipAdapter(ArrayList<QuotesModelClass> datalist, Context context, ClickValuePass clickValuePass) {
        this.datalist = datalist;
        this.context = context;
        this.clickValuePass = clickValuePass;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final QuotesModelClass modelClass = datalist.get(position);
        final String id = modelClass.getId();
        final String quotes = modelClass.getQuotes();
        final String catagory = modelClass.getCatagory();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.home_quotes_layout, container, false);
        TextView tvQuotes = view.findViewById(R.id.tvHomeQuotesLayoutQuotesId);
        LinearLayout linLayCopy = view.findViewById(R.id.linLayHomeQuotesLayoutCopy);
        LinearLayout linLayFav = view.findViewById(R.id.linLayHomeQuotesLayoutFav);
        LinearLayout linLayShare = view.findViewById(R.id.linLayHomeQuotesLayoutShare);
        final ImageView ivfavIcon = view.findViewById(R.id.ivHomeQuotesLayoutFavId);
        final TextView tvfavSelect = view.findViewById(R.id.tvHomeQuotesLayoutFavId);
        tvQuotes.setText(quotes);
        //Checking Favourite On Starting
        favChecker(view, id);
        //Copy Message
        linLayCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clickValuePass.ShowAd();
                copyText(view, v, quotes);

            }
        });
        //Sharing Text
        linLayShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quotes1 = modelClass.getQuotes();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, quotes1);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent.createChooser(intent, "Share via");
                context.startActivity(intent);
            }
        });
        //Set Favourite
        linLayFav.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.readFavData(id);
                clickValuePass.ShowAd();
                if (cursor.getCount() == 0) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    Boolean isUpdated = databaseHelper.updateData(id, quotes, catagory, "true");

                    if (isUpdated.equals(true)) {
                        Toast.makeText(context, "Added To Favourite", Toast.LENGTH_SHORT).show();
                        ivfavIcon.setImageResource(R.drawable.ic_favourite_select);
                        tvfavSelect.setTextColor(Color.parseColor("#4ECCA3"));
                    } else {
                        Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    Boolean isUpdated = databaseHelper.updateData(id, quotes, catagory, "false");
                    if (isUpdated.equals(true)) {
                        Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                        ivfavIcon.setImageResource(R.drawable.ic_favourite);
                        tvfavSelect.setTextColor(Color.WHITE);
                    } else {
                        Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        container.addView(view);
        return view;
    }

    private void copyText(View view, View v, String quotes) {
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Quotes", quotes);
        clipboard.setPrimaryClip(data);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ResourceAsColor")
    private void favChecker(View view, String id) {
        ImageView ivFavIcon = view.findViewById(R.id.ivHomeQuotesLayoutFavId);
        TextView tvFavSelect = view.findViewById(R.id.tvHomeQuotesLayoutFavId);
        databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.readFavData(id);
        if (cursor.getCount() == 0) {
            ivFavIcon.setImageResource(R.drawable.ic_favourite);
            tvFavSelect.setTextColor(Color.WHITE);
        } else {

            ivFavIcon.setImageResource(R.drawable.ic_favourite_select);
            tvFavSelect.setTextColor(Color.parseColor("#4ECCA3"));
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

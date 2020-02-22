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
import androidx.recyclerview.widget.RecyclerView;

import com.bangla_bani.bangla_quotes.Interface.ClickValuePass;
import com.bangla_bani.bangla_quotes.ModelClass.QuotesModelClass;
import com.bangla_bani.bangla_quotes.Helper.DatabaseHelper;
import com.trustedoffers.banglaquotes.R;

import java.util.ArrayList;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder> {
    private Context context;
    private ArrayList<QuotesModelClass> list;
    private DatabaseHelper databaseHelper;
    //Interface
    private ClickValuePass clickValuePass;

    public QuotesAdapter(Context context, ArrayList<QuotesModelClass> list, ClickValuePass clickValuePass) {
        this.context = context;
        this.list = list;
        this.clickValuePass = clickValuePass;
    }

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseHelper = new DatabaseHelper(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.quotes_layout, parent, false);
        return new QuotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuotesViewHolder holder, int position) {
        final QuotesModelClass modelClass = list.get(position);
        final String id = modelClass.getId();
        final String quotes = modelClass.getQuotes();
        final String catagory = modelClass.getCatagory();
        String fav = modelClass.getFavourite();
        //Checking Favourite On Starting
        favChecker(holder, id);
        holder.tvQuotes.setText(quotes);
        //Copy To Clipboard
        holder.linLayHolderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clickValuePass.ShowAd();
                copyText(holder, v);

            }
        });
        //Sharing Text
        holder.linLayHolderShare.setOnClickListener(new View.OnClickListener() {
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
        holder.linLayHolderFav.setOnClickListener(new View.OnClickListener() {
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
                        holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite_select);
                        holder.tvFavSelect.setTextColor(Color.parseColor("#4ECCA3"));
                    } else {
                        Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    Boolean isUpdated = databaseHelper.updateData(id, quotes, catagory, "false");
                    if (isUpdated.equals(true)) {
                        Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                        holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite);
                        holder.tvFavSelect.setTextColor(Color.WHITE);
                    } else {
                        Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void favChecker(QuotesViewHolder holder, String id) {
        Cursor cursor = databaseHelper.readFavData(id);
        if (cursor.getCount() == 0) {
            holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite);
            holder.tvFavSelect.setTextColor(Color.WHITE);
        } else {

            holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite_select);
            holder.tvFavSelect.setTextColor(Color.parseColor("#4ECCA3"));
        }
    }

    private void copyText(QuotesViewHolder holder, View v) {
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Quotes", holder.tvQuotes.getText().toString());
        clipboard.setPrimaryClip(data);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuotesViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuotes, tvFavSelect;
        private LinearLayout linLayHolderFav, linLayHolderCopy, linLayHolderShare;
        private ImageView ivHolderFavIcon;

        public QuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuotes = itemView.findViewById(R.id.tvQuotesLayoutQuotesId);
            tvFavSelect = itemView.findViewById(R.id.tvQuotesLayoutFavId);
            linLayHolderCopy = itemView.findViewById(R.id.linLayQuotesLayoutCopy);
            linLayHolderFav = itemView.findViewById(R.id.linLayQuotesLayoutFav);
            linLayHolderShare = itemView.findViewById(R.id.linLayQuotesLayoutShare);
            ivHolderFavIcon = itemView.findViewById(R.id.ivQuotesLayoutFavId);
        }
    }
}

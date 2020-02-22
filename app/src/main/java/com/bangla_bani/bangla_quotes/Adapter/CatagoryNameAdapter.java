package com.bangla_bani.bangla_quotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bangla_bani.bangla_quotes.Activity.QuotesActivity;
import com.bangla_bani.bangla_quotes.Interface.ClickValuePass;
import com.bangla_bani.bangla_quotes.ModelClass.CatagoryModelClass;
import com.trustedoffers.banglaquotes.R;

import java.util.ArrayList;

public class CatagoryNameAdapter extends RecyclerView.Adapter<CatagoryNameAdapter.CatagoryNameViewHolder> {
    private Context context;
    private ArrayList<CatagoryModelClass> list;
    //Interface
    private ClickValuePass clickValuePass;

    public CatagoryNameAdapter(Context context, ArrayList<CatagoryModelClass> list, ClickValuePass clickValuePass) {
        this.context = context;
        this.list = list;
        this.clickValuePass = clickValuePass;
    }

    @NonNull
    @Override
    public CatagoryNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.catagory_layout, parent, false);
        return new CatagoryNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatagoryNameViewHolder holder, final int position) {
        final CatagoryModelClass model = list.get(position);
        holder.tvHolderCatagoryName.setText(model.getBanglaCatagoryName());
        holder.ivHolderCatagoryImg.setImageResource(model.getImage());
        //Passing value To Quotes Activity
        holder.cvCatagoryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValuePass.ShowAd();
                String Catagory = model.getCatagory();
                String banlaCatagoryName = model.getBanglaCatagoryName();
                Intent intent = new Intent(context, QuotesActivity.class);
                intent.putExtra("Catagory", Catagory);
                intent.putExtra("BanglaCatagory", banlaCatagoryName);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchResult(ArrayList<CatagoryModelClass> data) {
        list = data;
        notifyDataSetChanged();
    }

    public class CatagoryNameViewHolder extends RecyclerView.ViewHolder {
        TextView tvHolderCatagoryName;
        ImageView ivHolderCatagoryImg;
        private CardView cvCatagoryId;

        public CatagoryNameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHolderCatagoryName = itemView.findViewById(R.id.tvCatagoryLayNameId);
            ivHolderCatagoryImg = itemView.findViewById(R.id.ivCatagoryLayImgId);
            cvCatagoryId = itemView.findViewById(R.id.cvCatagoryId);
        }
    }
}

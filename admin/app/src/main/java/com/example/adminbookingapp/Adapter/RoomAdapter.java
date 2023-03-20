package com.example.adminbookingapp.Adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminbookingapp.Admin.DetailHotelActivity;
import com.example.adminbookingapp.Model.Khachsan;
import com.example.adminbookingapp.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> implements Filterable{
    List<Khachsan> list;
    List<Khachsan> listfull;
    public RoomAdapter(List<Khachsan> list) {
        this.listfull = list;
        this.list = new ArrayList<>(listfull);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listlocation, parent, false);
        return new ViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Khachsan ks = list.get(position);

        holder.tenks.setText(ks.getTenks());
        holder.diachi.setText(ks.getDiachi());

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String get_gia = currencyVN.format(Integer.parseInt(ks.getGia()));
        holder.gia.setText(get_gia);

        if (ks.getHinh().equals("")) {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_image_24);
            holder.img.setImageDrawable(drawable);
        } else {
            Picasso.get().load(ks.getHinh())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentbk = new Intent(view.getContext(), DetailHotelActivity.class);
                intentbk.putExtra("clickdetail", ks);
                view.getContext().startActivity(intentbk);
            }
        });

        if (!ks.getTrangthai()) {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_circle_24);
            holder.statusks.setImageDrawable(drawable);
        } else {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_circle_24_green);
            holder.statusks.setImageDrawable(drawable);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Khachsan> newslist = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                newslist.addAll(listfull);
            }else{
                String newslist2 = charSequence.toString().toLowerCase(Locale.getDefault()).trim();

                for (Khachsan ks : listfull){
                    if(ks.getTenks().toLowerCase(Locale.getDefault()).contains(newslist2))
                        newslist.add(ks);
                    if(ks.getDiachi().toLowerCase().contains(newslist2))
                        newslist.add(ks);
                }
            }
            FilterResults results = new FilterResults();
            results.values = newslist;
            results.count = newslist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tenks, diachi, gia;
        RelativeLayout ln_linear;
        ImageView statusks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusks = itemView.findViewById(R.id.statusks);
            img = itemView.findViewById(R.id.img1);
            tenks = itemView.findViewById(R.id.tenkstext);
            diachi = itemView.findViewById(R.id.diachitext);
            gia = itemView.findViewById(R.id.giatext);
            ln_linear = itemView.findViewById(R.id.ln_linear);
        }
    }
}

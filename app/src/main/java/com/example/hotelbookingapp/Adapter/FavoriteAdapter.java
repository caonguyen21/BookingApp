package com.example.hotelbookingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.Model.Khachsan;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.UI.ActivityDetailHotel;
import com.example.hotelbookingapp.UI.FavoriteFragment;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    List<Khachsan> list;
    FavoriteFragment context;
    public FavoriteAdapter(FavoriteFragment context, List<Khachsan> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listlocation, parent, false);
        return new ViewHolder(v);
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
                    .placeholder(R.drawable.avatar)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentbk = new Intent(view.getContext(), ActivityDetailHotel.class);
                intentbk.putExtra("clickdetail", ks);
                view.getContext().startActivity(intentbk);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tenks, diachi, gia;
        RelativeLayout ln_linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            tenks = itemView.findViewById(R.id.tenkstext);
            diachi = itemView.findViewById(R.id.diachitext);
            gia = itemView.findViewById(R.id.giatext);
            ln_linear = itemView.findViewById(R.id.ln_linear);
        }
    }
}

package com.example.adminbookingapp.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.adminbookingapp.Model.Khachsan;
import com.example.adminbookingapp.Owner.DetailHoteOwnerlActivity;
import com.example.adminbookingapp.Owner.UpdateHotelActivity;
import com.example.adminbookingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomOwnerAdapter extends RecyclerView.Adapter<RoomOwnerAdapter.ViewHolder> {
    List<Khachsan> list;

    public RoomOwnerAdapter(List<Khachsan> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_owner, parent, false);
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
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .into(holder.img);
        }

        holder.ln_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentbk = new Intent(view.getContext(), DetailHoteOwnerlActivity.class);
                intentbk.putExtra("clickdetailOwner", ks);
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

        holder.edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sua = new Intent(view.getContext(), UpdateHotelActivity.class);
                sua.putExtra("clickEdit", ks);
                view.getContext().startActivity(sua);
            }
        });

        holder.delete_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("")
                        .setMessage("Bạn có muốn xóa khách sạn này không!")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("TPHCM");
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        list.clear();
                                        myRef.child(String.valueOf(ks.getTenks())).removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tenks, diachi, gia, delete_book, edit_book;
        ;
        RelativeLayout ln_linear;
        ImageView statusks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusks = itemView.findViewById(R.id.statusks2);
            img = itemView.findViewById(R.id.img2);
            tenks = itemView.findViewById(R.id.tenkstext2);
            diachi = itemView.findViewById(R.id.diachitext2);
            gia = itemView.findViewById(R.id.giatextonwer2);
            ln_linear = itemView.findViewById(R.id.ln_linear2);
            delete_book = itemView.findViewById(R.id.delete_book2);
            edit_book = itemView.findViewById(R.id.edit_book2);
        }
    }
}

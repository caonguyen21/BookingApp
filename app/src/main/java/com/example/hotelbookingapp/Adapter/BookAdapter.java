package com.example.hotelbookingapp.Adapter;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.Model.Booked;
import com.example.hotelbookingapp.Model.Khachsan;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.UI.DetailBookedActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    List<Booked> list;
    Khachsan ks;
    DatabaseReference reference;
    FirebaseAuth auth;

    public BookAdapter(List<Booked> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booked booked = list.get(position);

        holder.tenks.setText(booked.getTenks());
        holder.diachi.setText(booked.getDiachi());
        holder.ngayden.setText(booked.getNgayden());
        holder.ngaydi.setText(booked.getNgaydi());
        holder.tongtien.setText(booked.getTongtien());

        if (booked.getHinh().equals("")) {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_image_24);
            holder.img.setImageDrawable(drawable);
        } else {
            Picasso.get().load(booked.getHinh())
                    .placeholder(R.drawable.avatar)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentbk = new Intent(view.getContext(), DetailBookedActivity.class);
                intentbk.putExtra("phongdadat", booked);
                view.getContext().startActivity(intentbk);
            }
        });

        auth = FirebaseAuth.getInstance();
        holder.huydat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Bạn có muốn hủy đặt phòng này không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference = FirebaseDatabase.getInstance().getReference("phongdadat").child(auth.getUid());
                                reference.child(String.valueOf(booked.getTenks())).removeValue().addOnSuccessListener(unused -> {
                                    Toast.makeText(view.getContext(), "Hủy đặt phòng thành công!", Toast.LENGTH_SHORT).show();
                                });

                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, favorite;
        TextView tenks, diachi, ngayden, ngaydi, tongtien, huydat;
        RelativeLayout ln_linear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favorite = itemView.findViewById(R.id.favorite);
            img = itemView.findViewById(R.id.img2);
            tenks = itemView.findViewById(R.id.tenkstext2);
            diachi = itemView.findViewById(R.id.diachitext2);
            huydat = itemView.findViewById(R.id.btnhuydatphong);
            ln_linear = itemView.findViewById(R.id.ln_linear);
            tongtien = itemView.findViewById(R.id.itemtongtien);
            ngayden = itemView.findViewById(R.id.txt_ngayden);
            ngaydi = itemView.findViewById(R.id.txt_ngaydi);
        }
    }
}

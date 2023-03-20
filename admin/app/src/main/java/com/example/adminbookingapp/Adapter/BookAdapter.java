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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminbookingapp.Model.Booked;
import com.example.adminbookingapp.Owner.DetailBookedActivity;
import com.example.adminbookingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    List<Booked> list;
    DatabaseReference reference;

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
        holder.ngayden.setText(booked.getNgayden());
        holder.ngaydi.setText(booked.getNgaydi());
        holder.tongtien.setText(booked.getTongtien());
        holder.tenkh.setText(booked.getTenkhachhang());

        holder.ln_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentbk = new Intent(view.getContext(), DetailBookedActivity.class);
                intentbk.putExtra("detailbooked", booked);
                view.getContext().startActivity(intentbk);
            }
        });

        if (!booked.getStatus()) {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_circle_24);
            holder.statusks.setImageDrawable(drawable);
        } else {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_circle_24_green);
            holder.statusks.setImageDrawable(drawable);
        }

        holder.onTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("phongdadat");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String uid = child.getKey();
                            reference = FirebaseDatabase.getInstance().getReference("phongdadat").child(uid);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        Booked value = child.getValue(Booked.class);
                                        if (value.getTenks().equals(booked.getTenks())) {
                                            reference.child(booked.getTenks()).child("status").setValue(true);
                                            Toast.makeText(v.getContext(), "Đã duyệt đơn hàng!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Warning!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.xoadonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Bạn có muốn xóa đơn đặt phòng này không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference = FirebaseDatabase.getInstance().getReference("phongdadat");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            String uid = child.getKey();
                                            reference = FirebaseDatabase.getInstance().getReference("phongdadat").child(uid);
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot child : snapshot.getChildren()) {
                                                        Booked value = child.getValue(Booked.class);
                                                        if (value.getTenks().equals(booked.getTenks())) {
                                                            reference.child(booked.getTenks()).removeValue();
                                                            Toast.makeText(view.getContext(), "Đã xóa đơn đặt phòng!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
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
        TextView tenks, ngayden, ngaydi, tongtien, tenkh, onTrangThai, xoadonhang;
        RelativeLayout ln_linear;
        ImageView statusks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            xoadonhang = itemView.findViewById(R.id.xoadonhang);
            onTrangThai = itemView.findViewById(R.id.duyetdon);
            statusks = itemView.findViewById(R.id.statusbooked);
            tenks = itemView.findViewById(R.id.tenkstext2);
            tenkh = itemView.findViewById(R.id.tenkhachhang);
            ln_linear = itemView.findViewById(R.id.deltaRelative);
            tongtien = itemView.findViewById(R.id.itemtongtien);
            ngayden = itemView.findViewById(R.id.txt_ngayden);
            ngaydi = itemView.findViewById(R.id.txt_ngaydi);
        }
    }
}

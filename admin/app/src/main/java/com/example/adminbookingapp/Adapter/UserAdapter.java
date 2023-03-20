package com.example.adminbookingapp.Adapter;

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

import com.example.adminbookingapp.Model.User;
import com.example.adminbookingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<User> list;

    public UserAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);

        holder.tenuser.setText(user.getUsername());
        holder.emailuser.setText(user.getEmail());
        holder.sdtuser.setText(user.getPhone());

        if (user.getImage().equals("1")) {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_outline_account_circle_24);
            holder.img.setImageDrawable(drawable);
        } else {
            Picasso.get().load(user.getImage())
                    .placeholder(R.drawable.ic_outline_account_circle_24)
                    .fit()
                    .noFade()
                    .centerCrop()
                    .into(holder.img);
        }
        if (!user.isStatus()) {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_circle_24);
            holder.status.setImageDrawable(drawable);
        } else {
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.ic_baseline_circle_24_green);
            holder.status.setImageDrawable(drawable);
        }

        holder.onOffTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user1 = child.getValue(User.class);
                            if (user1.getEmail().equals(user.getEmail())) {
                                if (user1.isStatus()) {
                                    reference.child(child.getKey()).child("status").setValue(false);
                                    Toast.makeText(v.getContext(), "Khóa tài khoản người dùng!", Toast.LENGTH_SHORT).show();
                                } else {
                                    reference.child(child.getKey()).child("status").setValue(true);
                                    Toast.makeText(v.getContext(), "Mở tài khoản người dùng!", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Warning!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, status;
        TextView tenuser, emailuser, sdtuser;
        RelativeLayout relativeLayout;
        private TextView onOffTrangThai;
        //  private SwipeLayout swipeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //  swipeLayout = itemView.findViewById(R.id.swipeAccount);
            status = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.avatarUser);
            tenuser = itemView.findViewById(R.id.tenUser);
            emailuser = itemView.findViewById(R.id.emailUser);
            sdtuser = itemView.findViewById(R.id.sdtUser);
            relativeLayout = itemView.findViewById(R.id.ln_linear);
            onOffTrangThai = itemView.findViewById(R.id.xoataikhoan);
        }
    }
}

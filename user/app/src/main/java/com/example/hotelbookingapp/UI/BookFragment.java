package com.example.hotelbookingapp.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.Adapter.BookAdapter;
import com.example.hotelbookingapp.Adapter.RoomAdapter;
import com.example.hotelbookingapp.Model.Booked;
import com.example.hotelbookingapp.Model.Khachsan;
import com.example.hotelbookingapp.Model.User;
import com.example.hotelbookingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    TextView idxinchao, empty, empty2;
    DatabaseReference reference;
    BookAdapter bookAdapter;
    List<Booked> list;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    private String userID;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        initUI(view);

        //Toolbar welcome
        wellcome();

        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("phongdadat").child(auth.getUid());
        recyclerView = view.findViewById(R.id.rcv_book);
        //phan cach giua cac item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        bookAdapter = new BookAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);
        //thay doi layout khi co item recyclerview
        bookAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (bookAdapter != null && view != null) {
                    if (bookAdapter.getItemCount() == 0) {
                        empty.setVisibility(View.VISIBLE);
                        empty2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.GONE);
                        empty2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        getListBook();

        return view;
    }

    private void initUI(View view) {
        idxinchao = view.findViewById(R.id.xinchao_id);
        empty = view.findViewById(R.id.text1);
        empty2 = view.findViewById(R.id.text2);
    }

    private void getListBook() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Booked booked = child.getValue(Booked.class);
                    list.add(booked);
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void wellcome() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                String ten = userprofile.username;
                idxinchao.setText("Xin chào, " + ten + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                idxinchao.setText("Đặt chỗ");
            }
        });
    }

}
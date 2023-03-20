package com.example.adminbookingapp.Owner;

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

import com.example.adminbookingapp.Adapter.BookAdapter;
import com.example.adminbookingapp.Model.Booked;
import com.example.adminbookingapp.Model.Owner;
import com.example.adminbookingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QLDonHangFragment extends Fragment {

    TextView empty, empty2;
    DatabaseReference reference;
    BookAdapter bookAdapter;
    List<Booked> list;
    RecyclerView recyclerView;
    String ten = "";
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
        View view = inflater.inflate(R.layout.fragment_q_l_don_hang, container, false);
        initUI(view);

        //Toolbar welcome
        wellcome();
        recyclerView = view.findViewById(R.id.rcv_book);
        reference = FirebaseDatabase.getInstance().getReference("phongdadat");
        list = new ArrayList<>();
        getListBook();


        //phan cach giua cac item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        bookAdapter = new BookAdapter(list);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        return view;
    }

    private void initUI(View view) {
        empty = view.findViewById(R.id.text1);
        empty2 = view.findViewById(R.id.text2);
    }

    private void getListBook() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String uid = child.getKey();
                    reference = FirebaseDatabase.getInstance().getReference("phongdadat").child(uid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Booked value = child.getValue(Booked.class);
                                if (value.getTenks().equals(ten)) {
                                    list.add(value);
                                }
                            }
                            bookAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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
        reference = FirebaseDatabase.getInstance().getReference("Owner");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Owner userprofile = snapshot.getValue(Owner.class);
                ten = userprofile.getTenks();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
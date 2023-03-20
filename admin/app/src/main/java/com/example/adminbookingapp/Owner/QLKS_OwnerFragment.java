package com.example.adminbookingapp.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adminbookingapp.Adapter.RoomOwnerAdapter;
import com.example.adminbookingapp.Model.Khachsan;
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

public class QLKS_OwnerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TextView idxinchao, empty;
    Button empty2;
    DatabaseReference referenceip;
    RoomOwnerAdapter roomOwnerAdapter;
    List<Khachsan> list;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
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
        View view = inflater.inflate(R.layout.fragment_q_l_k_s__owner, container, false);
        initUI(view);
        //Toolbar welcome
        wellcome();
        auth = FirebaseAuth.getInstance();
        referenceip = FirebaseDatabase.getInstance().getReference("TPHCM");
        recyclerView = view.findViewById(R.id.rcv_khachsan);
        list = new ArrayList<>();
        getListBook();
        //phan cach giua cac item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        roomOwnerAdapter = new RoomOwnerAdapter(list);
        recyclerView.setAdapter(roomOwnerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //thay doi layout khi co item recyclerview
        roomOwnerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (roomOwnerAdapter != null && view != null) {
                    if (roomOwnerAdapter.getItemCount() == 0) {
                        empty.setVisibility(View.VISIBLE);
                        empty2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.GONE);
                        empty2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void initUI(View view) {
        idxinchao = view.findViewById(R.id.xinchao_id);
        empty = view.findViewById(R.id.text1);
        empty2 = view.findViewById(R.id.btnthemks);
        empty2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddHotelActivity.class));
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
    }

    public void getListBook() {
        referenceip.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan diaDiem = child.getValue(Khachsan.class);
                    if (diaDiem.getTenks().equals(ten)) {
                        list.add(diaDiem);
                    }
                }
                roomOwnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void wellcome() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        referenceip = FirebaseDatabase.getInstance().getReference("Owner");
        referenceip.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Owner userprofile = snapshot.getValue(Owner.class);
                ten = userprofile.getTenks();
                String tenuser = userprofile.getUsername();
                idxinchao.setText("Xin chào, " + tenuser + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                idxinchao.setText("");
            }
        });
    }

    @Override
    public void onRefresh() {
        getListBook();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
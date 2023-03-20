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

import com.example.hotelbookingapp.Adapter.FavoriteAdapter;
import com.example.hotelbookingapp.Model.Khachsan;
import com.example.hotelbookingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    TextView empty, empty2;
    View view;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FavoriteAdapter favoriteAdapter;
    private FirebaseAuth auth;
    private List<Khachsan> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        empty = view.findViewById(R.id.empty_view);
        empty2 = view.findViewById(R.id.empty_view2);

        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User").child(auth.getUid()).child("Favorites");
        recyclerView = view.findViewById(R.id.rcv_favorite);
        //phan cach giua cac item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        favoriteAdapter = new FavoriteAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(favoriteAdapter);
        favoriteAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (favoriteAdapter != null && view != null) {
                    if (favoriteAdapter.getItemCount() == 0) {
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
        getListFavorites();

        return view;
    }

    private void getListFavorites() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan diaDiem = child.getValue(Khachsan.class);
                    list.add(diaDiem);
                }
                favoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
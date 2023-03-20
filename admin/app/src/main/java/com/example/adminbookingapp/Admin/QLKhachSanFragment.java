package com.example.adminbookingapp.Admin;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminbookingapp.Adapter.RoomAdapter;
import com.example.adminbookingapp.Model.Khachsan;
import com.example.adminbookingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QLKhachSanFragment extends Fragment {
    List<Khachsan> list;
    RecyclerView recyclerView;
    RoomAdapter apdapterRoom;
    SearchView search_view;
    Toolbar toolbar;
    MenuItem menuItem;
    String[] ks = {"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7", "Quận 8", "Quận 9", "Quận 10"};
    private DatabaseReference reference;
    private FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_q_l_khach_san, container, false);
        initUI(view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");
        toolbar.setOverflowIcon(ContextCompat.getDrawable(activity, R.drawable.ic_baseline_sort_24));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("TPHCM");
        list = new ArrayList<>();

        //phan cach giua cac item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getlist();
        return view;
    }

    private void initUI(View view) {
        recyclerView = view.findViewById(R.id.Rcv);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void getlist() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan ks = child.getValue(Khachsan.class);
                    list.add(ks);
                }
                apdapterRoom = new RoomAdapter(list);
                recyclerView.setAdapter(apdapterRoom);
                apdapterRoom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getlistOn() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan ks = child.getValue(Khachsan.class);
                    if (ks.getTrangthai().equals(true)) {
                        list.add(ks);
                    }
                }
                apdapterRoom = new RoomAdapter(list);
                recyclerView.setAdapter(apdapterRoom);
                apdapterRoom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getlistOff() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan ks = child.getValue(Khachsan.class);
                    if (ks.getTrangthai().equals(false)) {
                        list.add(ks);
                    }
                }
                apdapterRoom = new RoomAdapter(list);
                recyclerView.setAdapter(apdapterRoom);
                apdapterRoom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //gia cao toi thap
    private void hightolow() {
        Query query = reference.orderByChild("gia");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan ks = child.getValue(Khachsan.class);
                    list.add(0, ks);
                }
                apdapterRoom = new RoomAdapter(list);
                recyclerView.setAdapter(apdapterRoom);
                apdapterRoom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //gia thap toi cao
    private void lowtohigh() {
        Query query = reference.orderByChild("gia");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan ks = child.getValue(Khachsan.class);
                    list.add(ks);
                }
                apdapterRoom = new RoomAdapter(list);
                recyclerView.setAdapter(apdapterRoom);
                apdapterRoom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Khong the lay du lieu!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);

        //search theo ten
        menuItem = menu.findItem(R.id.menu_search);
        search_view = (SearchView) MenuItemCompat.getActionView(menuItem);
        search_view.setIconified(true);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search_view.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search_view.setQueryHint("Tìm kiếm...");
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                apdapterRoom.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_hightolow:
                hightolow();
                break;

            case R.id.menu_lowtohigh:
                lowtohigh();
                break;

            case R.id.menu_fillerQuan:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Chọn quận muốn tìm");
                // add a list
                builder.setItems(ks, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //quan1
                                search_view.setQuery("2", true);
                            case 1: // quan2
                                search_view.setQuery("2", true);
                            case 2: // camel
                                search_view.setQuery("2", true);
                            case 3: // sheep
                                search_view.setQuery("2", true);
                            case 4: // goat
                                search_view.setQuery("2", true);
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            case R.id.menu_xoaFiller:
                getlist();
                break;

            case R.id.menu_statusOn:
                getlistOn();
                break;

            case R.id.menu_statusOff:
                getlistOff();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
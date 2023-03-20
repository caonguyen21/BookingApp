package com.example.adminbookingapp.Owner;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.adminbookingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class OwnewMainScreem extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    QLKS_OwnerFragment qlks_ownerFragment = new QLKS_OwnerFragment();
    QLDonHangFragment qlDonHangFragment = new QLDonHangFragment();
    OwnerFragment ownerFragment = new OwnerFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownew_main_screem);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, qlks_ownerFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_qlkhachsanowner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qlks_ownerFragment).commit();
                        return true;
                    case R.id.menu_qlDonhang:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qlDonHangFragment).commit();
                        return true;
                    case R.id.menu_hoso:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, ownerFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }

}
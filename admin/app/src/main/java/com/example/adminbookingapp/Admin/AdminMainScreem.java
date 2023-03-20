package com.example.adminbookingapp.Admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminbookingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainScreem extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    QLKhachSanFragment qlKhachSanFragment = new QLKhachSanFragment();
    QLKhachHangFragment qlKhachHangFragment = new QLKhachHangFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, qlKhachSanFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_qlkhachsan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qlKhachSanFragment).commit();
                        return true;
                    case R.id.menu_qlkhachhang:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qlKhachHangFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }
}

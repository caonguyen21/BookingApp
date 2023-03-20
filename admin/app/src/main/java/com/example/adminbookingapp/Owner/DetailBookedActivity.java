package com.example.adminbookingapp.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminbookingapp.Model.Booked;
import com.example.adminbookingapp.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailBookedActivity extends AppCompatActivity {
    TextView txttenks, txtgiaphong, txttongtien, hetphong, edt_chonngayden, edt_chonngaydi, mail, tenkhachhang, sdtkh;
    ImageView img;
    Booked booked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booked);

        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_detaibooked);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        txttenks = findViewById(R.id.tenks);
        txtgiaphong = findViewById(R.id.gia);
        img = findViewById(R.id.imgdp);
        edt_chonngaydi = findViewById(R.id.edt_chonngaydi);
        edt_chonngayden = findViewById(R.id.edt_chonngayden);
        hetphong = findViewById(R.id.hetphong);
        txttongtien = findViewById(R.id.txttongtien);
        mail = findViewById(R.id.mail);
        tenkhachhang = findViewById(R.id.tenkhachhang);
        sdtkh = findViewById(R.id.sdtkh);

        //get data
        Intent intentdp = getIntent();
        booked = (Booked) intentdp.getSerializableExtra("detailbooked");
        mail.setText(booked.getTenkhachhang());
        tenkhachhang.setText(booked.getTenkh());
        sdtkh.setText(booked.getSdtkh());
        Picasso.get().load(booked.getHinh()).fit().centerCrop().into(img);
        txttenks.setText(booked.getTenks());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String get_gia = currencyVN.format(Integer.parseInt(booked.getGia()));
        txtgiaphong.setText(get_gia);
        edt_chonngaydi.setText(booked.getNgaydi());
        edt_chonngayden.setText(booked.getNgayden());
        txttongtien.setText(booked.getTongtien());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
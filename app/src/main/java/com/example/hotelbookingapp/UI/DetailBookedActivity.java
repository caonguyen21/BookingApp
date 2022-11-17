package com.example.hotelbookingapp.UI;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.hotelbookingapp.Model.Booked;
import com.example.hotelbookingapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import kr.co.prnd.readmore.ReadMoreTextView;

public class DetailBookedActivity extends AppCompatActivity {
    Booked booked;
    TextView txttenks2, txtdiachi2, txtgia2, sdt, toolbartenks;
    ReadMoreTextView txtmota2;
    ImageView img2s, img22, img32, img42;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailbooked);

        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_booked);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbartenks = findViewById(R.id.toolbar_booked1);
        //end
        txttenks2 = findViewById(R.id.txtTenks2);
        txtdiachi2 = findViewById(R.id.txtDiachi2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        txtdiachi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
        txtgia2 = findViewById(R.id.txtGia2);
        txtmota2 = findViewById(R.id.txtMoto2);
        img2s = findViewById(R.id.img2s);
        img22 = findViewById(R.id.img22);
        img32 = findViewById(R.id.img32);
        img42 = findViewById(R.id.img42);
        sdt = findViewById(R.id.sdtks);
        sdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + booked.getSdtks()));
                isTelephonyEnabled();
                startActivity(intent);
            }
        });

        Intent intentbk = getIntent();
        booked = (Booked) intentbk.getSerializableExtra("phongdadat");

        txttenks2.setText(booked.getTenks());
        String ten = booked.getTenks();
        toolbartenks.setText(ten);
        txtdiachi2.setText(booked.getDiachiCT());
        txtmota2.setText(booked.getMota());
        sdt.setText(booked.getSdtks());

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String get_gia = currencyVN.format(Integer.parseInt(booked.getGia()));
        txtgia2.setText(get_gia);
        Picasso.get().load(booked.getHinh()).fit().centerCrop().into(img2s);
        Picasso.get().load(booked.getHinh2()).fit().centerCrop().into(img22);
        Picasso.get().load(booked.getHinh3()).fit().centerCrop().into(img32);
        Picasso.get().load(booked.getHinh4()).fit().centerCrop().into(img42);

    }

    private boolean isTelephonyEnabled() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            ActivityCompat.requestPermissions(DetailBookedActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(DetailBookedActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        try {
                            Uri uri;
                            uri = Uri.parse("https://www.google.com/maps/dir/" + location.getLatitude() + ' ' + location.getLongitude() + '/' + booked.getDiachiCT());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setPackage("com.google.android.apps.maps");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
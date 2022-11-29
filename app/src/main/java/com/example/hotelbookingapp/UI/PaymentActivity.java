package com.example.hotelbookingapp.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hotelbookingapp.Model.Khachsan;
import com.example.hotelbookingapp.Model.User;
import com.example.hotelbookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    Spinner chongayedt;
    ImageView img;
    Button btndatphong;
    TextView txttenks, txtgiaphong, txttongtien, hetphong, chonkhachsan, text1, text2;
    Khachsan ks;
    Toolbar toolbar;
    EditText edt_chonngayden, edt_chonngaydi;
    Calendar calendarOne, calendarTwo;
    String userId;
    String[] theloai = {"Phòng đơn", "Phòng đôi"};
    private DatabaseReference reference;
    private DatePickerDialog picker;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_pay);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        //endtoolbar

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //spiner
        chongayedt = findViewById(R.id.chongayedt);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentActivity.this, android.R.layout.simple_spinner_item, theloai);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chongayedt.setAdapter(arrayAdapter);


        //end spiner
        txttenks = findViewById(R.id.tenks);
        txtgiaphong = findViewById(R.id.gia);
        img = findViewById(R.id.imgdp);
        btndatphong = findViewById(R.id.btndatphongdp);
        edt_chonngaydi = findViewById(R.id.edt_chonngaydi);
        edt_chonngayden = findViewById(R.id.edt_chonngayden);
        hetphong = findViewById(R.id.hetphong);
        txttongtien = findViewById(R.id.txttongtien);
        chonkhachsan = findViewById(R.id.chonkhachsan);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        //get data
        Intent intentdp = getIntent();
        ks = (Khachsan) intentdp.getSerializableExtra("clickdp");
        txttenks.setText(ks.getTenks());
        String settenks = ks.getTenks();

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
       /* String get_gia = currencyVN.format(Integer.parseInt(ks.getGia()));
        txtgiaphong.setText(get_gia);*/

      /*  String get_gia2 = currencyVN.format(Integer.parseInt(ks.getGia()));
        txttongtien.setText(get_gia2);*/

        Picasso.get().load(ks.getHinh()).fit().centerCrop().into(img);
        chonkhachsan.setText(settenks);

        //ngay đến
        edt_chonngayden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayden();
            }
        });

        //ngay di
        edt_chonngaydi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngaydi();
            }
        });

        //   readdata();

        btndatphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ngayden = edt_chonngayden.getText().toString();
                String ngaydi = edt_chonngaydi.getText().toString();

                if (ngayden.isEmpty()) {
                    edt_chonngayden.setError("");
                    Toast.makeText(PaymentActivity.this, "Vui lòng nhập ngày nhận phòng!", Toast.LENGTH_SHORT).show();
                    edt_chonngayden.requestFocus();
                    return;
                }

                if (ngaydi.isEmpty() && ngayden.isEmpty()) {
                    edt_chonngaydi.setError("");
                    Toast.makeText(PaymentActivity.this, "Vui lòng nhập ngày trả phòng!", Toast.LENGTH_SHORT).show();
                    edt_chonngaydi.requestFocus();
                    return;
                }

                String strtenks = txttenks.getText().toString();
                reference = FirebaseDatabase.getInstance().getReference("TPHCM");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Khachsan ks1 = child.getValue(Khachsan.class);
                            if (child.getKey().equals(strtenks)) {
                                updateKs(ks1.getTenks(), ks1.getDiachi(), ks1.getDiachiCT(), ks1.getGia(), ks1.getMota(), ks1.getHinh(), ks1.getHinh2(), ks1.getHinh3(), ks1.getHinh4(), ks1.getSlphongdon(), ks1.getSdtks());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        chongayedt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //  String get_gia = currencyVN.format(Integer.parseInt());
                        txtgiaphong.setText(ks.getGia());
                        ngaydi();
                        ngayden();
                        tongtien(txtgiaphong.getText().toString());
                        break;
                    case 1:
                        //   String gia = currencyVN.format(Integer.parseInt(ks.getGia2()));
                        txtgiaphong.setText(ks.getGia2());
                        ngaydi();
                        ngayden();
                        tongtien(txtgiaphong.getText().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void readdata() {
        String email = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("User");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (child.getKey().equals(email)) {
                        text1.setText(user.getUsername().toString());
                        text2.setText(user.getPhone().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateKs(String tenks, String diachi, String diachiCT, String gia, String mota, String hinh, String hinh2, String hinh3, String hinh4, String slphongdon, String sdtks) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String strtenks = txttenks.getText().toString().trim();
        String email = user.getUid();
        String emailkh = user.getEmail();
        String ngayden = edt_chonngayden.getText().toString();
        String ngaydi = edt_chonngaydi.getText().toString();
        String tongtien = txttongtien.getText().toString();
        String tenkh = text1.getText().toString();
        String sdt = text2.getText().toString();
        DatabaseReference myref = database.getReference("phongdadat");
        Map<String, Object> map = new HashMap<>();
        map.put("ngayden", ngayden);
        map.put("ngaydi", ngaydi);
        map.put("tongtien", tongtien);
        map.put("tenkhachhang", emailkh);
        map.put("tenkh", tenkh);
        map.put("sdtkh", sdt);
        map.put("status", false);
        myref.child(email).child(strtenks).updateChildren(map);


        HashMap ks = new HashMap();
        ks.put("tenks", tenks);
        ks.put("diachi", diachi);
        ks.put("gia", gia);
        ks.put("hinh", hinh);
        ks.put("mota", mota);
        ks.put("DiachiCT", diachiCT);
        ks.put("hinh2", hinh2);
        ks.put("hinh3", hinh3);
        ks.put("hinh4", hinh4);
        ks.put("slphongdon", slphongdon);
        ks.put("Sdtks", sdtks);

        reference = FirebaseDatabase.getInstance().getReference("phongdadat");
        reference.child(email).child(strtenks).updateChildren(ks).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Toast.makeText(PaymentActivity.this, "Đặt phòng thành công!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this, "Đặt phòng thất bại!", Toast.LENGTH_LONG).show();
            }
        });
        finish();
    }

    private void tongtien(String a) {
        int songay = (int) ((calendarTwo.getTimeInMillis() - calendarOne.getTimeInMillis()) / (1000 * 60 * 60 * 24));
        if (songay == 0) {
            songay = 1;
        }
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        Float giaphong = Float.parseFloat(a);
        Float tongtien = songay * giaphong;
        String tongtien2 = currencyVN.format(tongtien);
        txttongtien.setText(tongtien2);
        txttongtien.setVisibility(View.VISIBLE);
    }

    private void ngayden() {
        calendarOne = Calendar.getInstance();
        int day = calendarOne.get(Calendar.DAY_OF_MONTH);
        int month = calendarOne.get(Calendar.MONTH);
        int year = calendarOne.get(Calendar.YEAR);

        picker = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                calendarOne.set(year, month, dayofMonth);
                month = month + 1;
                edt_chonngayden.setText(dayofMonth + "/" + month + "/" + year);

            }
        }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    private void ngaydi() {
        String getfromdate = edt_chonngayden.getText().toString().trim();
        String[] getfrom = getfromdate.split("/");
        int year, month, day;
        year = Integer.parseInt(getfrom[2]);
        month = Integer.parseInt(getfrom[1]);
        day = Integer.parseInt(getfrom[0]);

        calendarTwo = Calendar.getInstance();
        month = month - 1;
        calendarTwo.set(year, month, day);
        picker = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                calendarTwo.set(year, month, dayofMonth);
                edt_chonngaydi.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                tongtien(txtgiaphong.getText().toString());
            }
        }, year, month, day + 1);
        picker.getDatePicker().setMinDate(calendarTwo.getTimeInMillis());
        picker.show();
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
package com.example.adminbookingapp.Owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminbookingapp.Model.Khachsan;
import com.example.adminbookingapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateHotelActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText suatenks, suadiachi, suadiachict, suamota, suagia, slphongdon, edt_suasdt;
    ImageView suaurl, suaurl2, suaurl3, suaurl4;
    Button sua, tv_selectImage2;
    Khachsan ks;
    private Uri mImageUri;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updataroom);
        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        suatenks = findViewById(R.id.edt_suatenks);
        suadiachi = findViewById(R.id.edt_suadiachi);
        suadiachict = findViewById(R.id.edt_suadiachict);
        suamota = findViewById(R.id.edt_suamota);
        suagia = findViewById(R.id.edt_suagia);
        suaurl = findViewById(R.id.suaurl);
        slphongdon = findViewById(R.id.edt_suasoluongphong);
        suaurl2 = findViewById(R.id.suaurl2);
        suaurl3 = findViewById(R.id.suaurl3);
        suaurl4 = findViewById(R.id.suaurl4);
        sua = findViewById(R.id.btn_suaks);
        edt_suasdt = findViewById(R.id.edt_suasdt);
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUpdateRoom();
            }
        });
        tv_selectImage2 = findViewById(R.id.tv_selectImage2);
        tv_selectImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoose();
            }
        });
        DisplayRoom();
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setAction((Intent.ACTION_GET_CONTENT));
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null && data.getClipData().getItemCount() == 4) {
                    int countGetClipData = data.getClipData().getItemCount();
                    for (int i = 0; i < countGetClipData; i++) {
                        if (i == 0) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(suaurl);
                        }
                        if (i == 1) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(suaurl2);
                        }
                        if (i == 2) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(suaurl3);
                        }
                        if (i == 3) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).into(suaurl4);
                        }
                        ImageList.add(mImageUri);
                    }
                    mImageUri = data.getClipData().getItemAt(0).getUri();
                    Picasso.get().load(mImageUri).into(suaurl);
                } else {
                    showToast("Chọn 4 tấm ảnh!");
                }
            }
        }
    }

    private void DisplayRoom() {
        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        ks = (Khachsan) intent.getSerializableExtra("clickEdit");
        suatenks.setText(ks.getTenks());
        suadiachi.setText(ks.getDiachi());
        suadiachict.setText(ks.getDiachiCT());
        suamota.setText(ks.getMota());
        suagia.setText(ks.getGia());
        slphongdon.setText(ks.getSlphongdon());
        edt_suasdt.setText(ks.getSdtks());

        Picasso.get().load(ks.getHinh()).fit().centerCrop().into(suaurl);
        Picasso.get().load(ks.getHinh2()).fit().centerCrop().into(suaurl2);
        Picasso.get().load(ks.getHinh3()).fit().centerCrop().into(suaurl3);
        Picasso.get().load(ks.getHinh4()).fit().centerCrop().into(suaurl4);
    }

    private void ClickUpdateRoom() {
        String strtenks = suatenks.getText().toString().trim();
        String strdiachi = suadiachi.getText().toString().trim();
        String strdiachict = suadiachict.getText().toString().trim();
        String strmota = suamota.getText().toString().trim();
        String strgia = suagia.getText().toString().trim();
        String strphongdon = slphongdon.getText().toString().trim();
        String strsdtks = edt_suasdt.getText().toString().trim();

        String hinh = ks.getHinh();
        String hinh2 = ks.getHinh2();
        String hinh3 = ks.getHinh3();
        String hinh4 = ks.getHinh4();
        Boolean trangthai = ks.getTrangthai();

        if (strtenks.isEmpty()) {
            suatenks.setError("Vui lòng nhập đủ thông tin!");
            suatenks.requestFocus();
            return;
        }
        if (strdiachi.isEmpty()) {
            suadiachi.setError("Vui lòng nhập đủ thông tin!");
            suadiachi.requestFocus();
            return;
        }
        if (strdiachict.isEmpty()) {
            suadiachict.setError("Vui lòng nhập đủ thông tin!");
            suadiachict.requestFocus();
            return;
        }
        if (strmota.isEmpty()) {
            suamota.setError("Vui lòng nhập đủ thông tin!");
            suamota.requestFocus();
            return;
        }
        if (strgia.isEmpty()) {
            suagia.setError("Vui lòng nhập đủ thông tin!");
            suagia.requestFocus();
            return;
        }

        if (strphongdon.isEmpty()) {
            slphongdon.setError("Vui lòng nhập đủ thông tin!");
            slphongdon.requestFocus();
            return;
        }

        if (strsdtks.isEmpty()) {
            edt_suasdt.setError("Vui lòng nhập đủ thông tin!");
            edt_suasdt.requestFocus();
            return;
        }

        reference = FirebaseDatabase.getInstance().getReference("TPHCM");
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Khachsan_Images");
        Khachsan ks = new Khachsan(hinh, strtenks, strdiachi, strgia, hinh2, hinh3, hinh4, strdiachict, strmota, strphongdon, strsdtks,trangthai );
        reference.child(strtenks).setValue(ks);
        if (ImageList.size() != 0) {
            for (int i = 0; i < 4; i++) {
                Uri uri = ImageList.get(i);
                final StorageReference ImageName = ImageFolder.child("Image" + uri.getLastPathSegment());
                int finalI = i;
                ImageName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        final Uri downloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {
                            if (finalI == 0) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("hinh", downloadUri.toString());
                                reference.child(strtenks).updateChildren(hashMap);
                            }
                            if (finalI == 1) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("hinh2", downloadUri.toString());
                                reference.child(strtenks).updateChildren(hashMap);
                            }
                            if (finalI == 2) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("hinh3", downloadUri.toString());
                                reference.child(strtenks).updateChildren(hashMap);
                            }
                            if (finalI == 3) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("hinh4", downloadUri.toString());
                                reference.child(strtenks).updateChildren(hashMap);
                            }
                        }
                    }
                });
            }
        }
        showToast("Cập nhật khách sạn thành công!");
        finish();
    }
    private void showToast(String mess) {
        Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
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


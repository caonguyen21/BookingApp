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

import com.example.adminbookingapp.Adapter.RoomOwnerAdapter;
import com.example.adminbookingapp.Model.Khachsan;
import com.example.adminbookingapp.Model.Owner;
import com.example.adminbookingapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AddHotelActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText tenks, diachi, diachict, mota, gia, slphongdon, sdtks;
    Button them;
    ImageView url, url2, url3, url4;
    Button tv_selectImage;
    Khachsan ks;
    RoomOwnerAdapter roomOwnerAdapter;
    String ksowner = "";
    private DatabaseReference reference;
    private Uri mImageUri;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        auth = FirebaseAuth.getInstance();
        //toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        tenks = findViewById(R.id.edt_tenks);
        diachi = findViewById(R.id.edt_diachi);
        diachict = findViewById(R.id.edt_diachict);
        mota = findViewById(R.id.edt_mota);
        gia = findViewById(R.id.edt_gia);
        tv_selectImage = findViewById(R.id.tv_selectImage);
        slphongdon = findViewById(R.id.edt_soluongphong);
        sdtks = findViewById(R.id.edt_sdt);

        url = findViewById(R.id.url);
        url2 = findViewById(R.id.url2);
        url3 = findViewById(R.id.url3);
        url4 = findViewById(R.id.url4);
        them = findViewById(R.id.btn_themks);

        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickAddRoom();
            }
        });
        tv_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });
        gettenks();
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
                            Picasso.get().load(mImageUri).fit().centerCrop().into(url);
                        }
                        if (i == 1) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).fit().centerCrop().into(url2);
                        }
                        if (i == 2) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).fit().centerCrop().into(url3);
                        }
                        if (i == 3) {
                            mImageUri = data.getClipData().getItemAt(i).getUri();
                            Picasso.get().load(mImageUri).fit().centerCrop().into(url4);
                        }
                        ImageList.add(mImageUri);
                    }
                    mImageUri = data.getClipData().getItemAt(0).getUri();
                    Picasso.get().load(mImageUri).fit().centerCrop().into(url);
                } else {
                    showToast("Chọn 4 tấm ảnh!");
                }
            }
        }
    }

    private void gettenks() {
        reference = FirebaseDatabase.getInstance().getReference("Owner");
        reference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Owner owner = snapshot.getValue(Owner.class);
                ksowner = owner.getTenks();
                tenks.setText(ksowner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ClickAddRoom() {
        String strtenks = tenks.getText().toString();
        String strdiachi = diachi.getText().toString().trim();
        String strdiachict = diachict.getText().toString().trim();
        String strmota = mota.getText().toString().trim();
        String strgia = gia.getText().toString().trim();
        String strphongdon = slphongdon.getText().toString().trim();
        String strsdtks = sdtks.getText().toString().trim();


        if (strdiachi.isEmpty()) {
            diachi.setError("Vui lòng nhập đủ thông tin!");
            diachi.requestFocus();
            return;
        }
        if (strdiachict.isEmpty()) {
            diachict.setError("Vui lòng nhập đủ thông tin!");
            diachict.requestFocus();
            return;
        }
        if (strmota.isEmpty()) {
            mota.setError("Vui lòng nhập đủ thông tin!");
            mota.requestFocus();
            return;
        }
        if (strgia.isEmpty()) {
            gia.setError("Vui lòng nhập đủ thông tin!");
            gia.requestFocus();
            return;
        }
        if (strphongdon.isEmpty()) {
            slphongdon.setError("Vui lòng nhập đủ thông tin!");
            slphongdon.requestFocus();
            return;
        }
        if (strsdtks.isEmpty()) {
            sdtks.setError("Vui lòng nhập đủ thông tin!");
            sdtks.requestFocus();
            return;
        }
        if (ImageList.size() != 4) {
            showToast("Vui lòng chọn 4 tấm ảnh");
            return;
        }

        reference = FirebaseDatabase.getInstance().getReference("TPHCM");
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Khachsan_Images");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean exist = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Khachsan ks = child.getValue(Khachsan.class);
                    if (ks.getTenks().equals(strtenks)) {
                        exist = true;
                        break;
                    }
                }

                if (exist == true) {
                    tenks.setError("Khách sạn đã tồn tại!");
                    tenks.requestFocus();
                    return;
                } else {
                    Khachsan ks = new Khachsan("", strtenks, strdiachi, strgia, "", "", "", strdiachict, strmota, strphongdon, strsdtks, false);
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
                    reference.child(strtenks).setValue(ks);
                    showToast("Đăng ký khách sạn của bạn thành công! Hãy đợi duyệt!");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Thêm khách sạn thất bại");
            }
        });
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

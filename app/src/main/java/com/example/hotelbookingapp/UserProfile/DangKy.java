package com.example.hotelbookingapp.UserProfile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DangKy extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText edt_Password, edt_Nhaplaipassword;
    Button DangKy;
    EditText edt_Hoten, edt_Sdt, edt_Email;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        mAuth = FirebaseAuth.getInstance();

        edt_Hoten = findViewById(R.id.edt_Hoten);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Sdt = findViewById(R.id.edt_Sdt);
        edt_Password = findViewById(R.id.edt_Password);
        edt_Nhaplaipassword = findViewById(R.id.edt_Nhaplaipassword);
        DangKy = findViewById(R.id.btn_Dangky);
        DangKy.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Dangky:
                DangKy();
                break;
        }
    }

    @SuppressLint("NotConstructor")
    private void DangKy() {
        String username = edt_Hoten.getText().toString().trim();
        String email = edt_Email.getText().toString().trim();
        String password = edt_Password.getText().toString().trim();
        String confirmpassword = edt_Nhaplaipassword.getText().toString().trim();
        String phone = edt_Sdt.getText().toString().trim();

        if (username.isEmpty()) {
            edt_Hoten.setError("Vui lòng nhập họ tên!");
            edt_Hoten.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            edt_Sdt.setError("Vui lòng nhập số điện thoại");
            edt_Sdt.requestFocus();
            return;
        }

        if (phone.length() > 12) {
            edt_Sdt.setError("Số điện thoại không hơn 12 ký tự!");
            edt_Sdt.requestFocus();
            return;
        }

        if (phone.length() < 9) {
            edt_Sdt.setError("Số điện thoại hơn 9 ký tự!");
            edt_Sdt.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edt_Email.setError("Vui lòng nhập email!");
            edt_Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_Email.setError("Email chưa hợp lệ!");
            edt_Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edt_Password.setError("Vui lòng nhập mật khẩu mới!");
            edt_Password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edt_Password.setError("Mật khẩu phải hơn 6 ký tự!");
            edt_Password.requestFocus();
            return;
        }

        if (confirmpassword.isEmpty()) {
            edt_Nhaplaipassword.setError("Vui lòng nhập lại mật khẩu mới!");
            edt_Nhaplaipassword.requestFocus();
            return;
        }

        if (!confirmpassword.equals(password)) {
            edt_Nhaplaipassword.setError("Không trùng với mật khẩu mới!");
            edt_Nhaplaipassword.requestFocus();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            databaseReference = FirebaseDatabase.getInstance().getReference("User");
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("username", username);
                            user.put("phone", phone);
                            databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user);
                            Toast.makeText(DangKy.this, "Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DangKy.this, DangNhap.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(DangKy.this, "Tạo tài khoản thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
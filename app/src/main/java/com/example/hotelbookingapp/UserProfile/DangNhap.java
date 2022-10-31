package com.example.hotelbookingapp.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.UI.MainScreen;
import com.example.hotelbookingapp.Model.User;
import com.example.hotelbookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity implements View.OnClickListener {

    TextView dangky, quenmatkhau;
    TextInputEditText password;
    EditText email;
    Button dangnhap;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        email = findViewById(R.id.edt_Email);
        password = findViewById(R.id.edt_Password);
        dangnhap = findViewById(R.id.btn_Dangnhap);
        dangnhap.setOnClickListener(this);
        dangky = findViewById(R.id.txt_Dangkytaikhoan);
        dangky.setOnClickListener(this);
        quenmatkhau = findViewById(R.id.txt_Quenmatkhau);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Dangnhap:
                CheckUser();
                break;
            case R.id.txt_Dangkytaikhoan:
                startActivity(new Intent(this, DangKy.class));
                break;
            /*case R.id.forgotpassword:
                startActivity(new Intent(this, activity_forgotpassword.class));
                break;*/
        }
    }

    private void CheckUser() {
        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString().trim();

        if (strEmail.isEmpty()) {
            email.setError("Vui lòng nhập email!");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            email.setError("Vui lòng nhập email hợp lệ");
            email.requestFocus();
            return;
        }

        if (strPassword.isEmpty()) {
            password.setError("Vui lòng nhập mật khẩu!");
            password.requestFocus();
            return;
        }

        if (strPassword.length() < 6) {
            password.setError("Mật khẩu phải hơn 6 ký tự!");
            password.requestFocus();
            return;
        }

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User(strEmail);
                }
            }
        });
    }

    private void User(String strEmail) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean exist = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (user.getEmail().equals(strEmail)) {
                        exist = true;
                        break;
                    }
                }
                if (exist == true) {
                    startActivity(new Intent(DangNhap.this, MainScreen.class));
                    finish();
                } else {
                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại! Hãy kiểm tra lại thông tin đăng nhập!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.hotelbookingapp.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelbookingapp.UserProfile.SignIn;
import com.example.hotelbookingapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ActivityUpdatePassword extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String uid;
    ProgressDialog pd;
    Button btn_Back, btn_Update;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference("Users");
        btn_Update = findViewById(R.id.btn_Update);
        btn_Back = findViewById(R.id.btn_Back);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
                startActivity(new Intent(ActivityUpdatePassword.this, SignIn.class));
                finish();
            }
        });
    }

    private void showPasswordChangeDailog() {
        final TextInputEditText oldpass = findViewById(R.id.oldpasslog);
        final TextInputEditText newpass = findViewById(R.id.newpasslog);
        final TextInputEditText verifypass = findViewById(R.id.verifynewpasslog);
        String oldp = oldpass.getText().toString().trim();
        String newp = newpass.getText().toString().trim();
        String verifyp = verifypass.getText().toString().trim();
        if (TextUtils.isEmpty(oldp)) {
            oldpass.setError("Current Password cant be empty");
            oldpass.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(newp)) {
            newpass.setError("New Password cant be empty");
            newpass.requestFocus();
            return;
        }
        if (newp.length() < 6) {
            newpass.setError("Password must not be less than 6 characters");
            newpass.requestFocus();
            return;
        }
        if (verifyp.length() < 6) {
            verifypass.setError("Password must not be less than 6 characters");
            verifypass.requestFocus();
            return;
        }
        if (!verifypass.getText().toString().trim().equals(newp)) {
            verifypass.setError("Password does not match confirm password");
            verifypass.requestFocus();
            return;
        }
        if (newpass.getText().toString().trim().equals(oldp)) {
            newpass.setError("New password must not be the same as the current password");
            newpass.requestFocus();
            return;
        }
        updatePassword(oldp, newp);

    }

    // Now we will check that if old password was authenticated
    // correctly then we will update the new password
    private void updatePassword(String oldp, final String newp) {
        pd.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldp);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newp)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(ActivityUpdatePassword.this, "Changed Password", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ActivityUpdatePassword.this, "Failed to change password", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ActivityUpdatePassword.this, "Current password is incorrect", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();

    }
}
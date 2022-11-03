package com.example.hotelbookingapp.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hotelbookingapp.Model.User;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.UserProfile.SignIn;
import com.example.hotelbookingapp.UserProfile.EditProfilePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    private ImageView updateImage;
    private MaterialCardView updateProfile, updatePassword;
    private TextInputEditText edt_email;
    private TextInputEditText edt_phone;
    private TextInputEditText edt_name;
    private CircleImageView profile_image;
    private String userId;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private Button btn_save, btn_logout;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_user, container, false);

        // Inflate the layout for this fragment
        initUI(view);
        DisplayProfile();

        updateImage.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), EditProfilePage.class);
            requireActivity().startActivity(intent);
        });

        /*updatePassword.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), ActivityUpdatePassword.class);
            requireActivity().startActivity(intent);
        });*/

        updateProfile.setOnClickListener(view13 -> {
            btn_save.setVisibility(View.VISIBLE);
            edt_name.setEnabled(true);
            edt_phone.setEnabled(true);
        });

        btn_save.setOnClickListener(view14 -> {
            String username = Objects.requireNonNull(edt_name.getText()).toString();
            String phone = Objects.requireNonNull(edt_phone.getText()).toString();
            updateDate(username, phone);
            btn_save.setVisibility(View.INVISIBLE);
            edt_name.setEnabled(false);
            edt_phone.setEnabled(false);
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initUI(View view) {
        edt_email = view.findViewById(R.id.tiet_email);
        edt_phone = view.findViewById(R.id.tiet_phone);
        edt_name = view.findViewById(R.id.tiet_fullname);
        updateImage = view.findViewById(R.id.updateImage);
        updateProfile = view.findViewById(R.id.updateProfile);
        updatePassword = view.findViewById(R.id.updatePassword);
        progressDialog = new ProgressDialog(getActivity());
        profile_image = view.findViewById(R.id.profile_image);
        btn_save = view.findViewById(R.id.btn_Save);
        btn_logout = view.findViewById(R.id.btn_logout);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void DisplayProfile() {
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userId = auth.getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals(userId)) {
                        user = child.getValue(User.class);
                        assert user != null;
                        edt_email.setText(user.getEmail());
                        edt_phone.setText(user.getPhone());
                        edt_name.setText(user.getUsername());
                        if (user.getImage() == null) {
                            Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                            profile_image.setImageDrawable(drawable);
                        } else {
                            Picasso.get().load(user.getImage()).into(profile_image);
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateDate(String username, String phone) {

        if (edt_name.getText().toString().trim().isEmpty()) {
            edt_name.setError("Họ tên không được để trống");
            edt_name.requestFocus();
            return;
        }

        if (edt_phone.getText().toString().length() != 10) {
            edt_phone.setError("Số điện thoại không đúng định dạng!");
            edt_phone.requestFocus();
            return;
        }


        HashMap User = new HashMap<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        User.put("username", username);
        User.put("phone", phone);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(userId).updateChildren(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Cập nhập thông tin thành công!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
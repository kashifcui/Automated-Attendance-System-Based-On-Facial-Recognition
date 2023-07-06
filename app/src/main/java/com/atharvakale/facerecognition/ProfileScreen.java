package com.atharvakale.facerecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileScreen extends AppCompatActivity {
    EditText uname, uemail, upassword;
    AppCompatButton signupbutton;
    FirebaseAuth mauth;
    FirebaseFirestore db;
        FirebaseUser curentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        uname = findViewById(R.id.nameSignup_edit);
        uemail = findViewById(R.id.email_signup_edit);
        mauth = FirebaseAuth.getInstance();
        curentUserId=mauth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
//        mauth.getCurrentUser().getUid();
        db.collection("profile").document(curentUserId.getUid()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve the value of a specific field
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            uname.setText(name);
                            uemail.setText(email);
                        } else {
                            Log.d("TAG", "Document does not exist");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Error retrieving value: " + e.getMessage());
            }
        });
    }
}
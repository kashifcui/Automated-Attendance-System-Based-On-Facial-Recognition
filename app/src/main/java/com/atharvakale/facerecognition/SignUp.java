package com.atharvakale.facerecognition;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText uname,uemail,upassword;
    AppCompatButton signupbutton;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    Map<String, Object> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        uname=findViewById(R.id.nameSignup_edit);
        uemail=findViewById(R.id.email_signup_edit);
        upassword=findViewById(R.id.password_signup_edit);
        mauth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        user = new HashMap<>();
        signupbutton=findViewById(R.id.btnSignUp);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringname, stringEmail, stringPassword;
                stringname = uname.getText().toString();
                stringEmail = uemail.getText().toString();
                stringPassword = upassword.getText().toString();
                if (stringname.isEmpty()) {
                    uname.setError("*Name");
                } else if (stringEmail.isEmpty()) {
                    uemail.setError("*Email");
                } else if (stringPassword.isEmpty()) {
                    upassword.setError("*Password");
                } else {
                    user.put("name", stringname);
                    user.put("email", stringEmail);
                    try {
                        mauth.createUserWithEmailAndPassword(stringEmail, stringPassword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(SignUp.this, "Create New Account Successfully", Toast.LENGTH_SHORT).show();
                                db.collection("profile").document(authResult.getUser().getUid()).set(user);
                                Intent intent =new Intent(SignUp.this,DashBoard.class);
                                finish();
                                startActivity(intent);
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+e.toString());
                                    }
                                });
                    } catch (Exception e) {
                        Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
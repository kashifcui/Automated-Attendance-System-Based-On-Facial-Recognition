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

public class LoginScreen extends AppCompatActivity {
    EditText email,password;
    AppCompatButton loginbutton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        email=findViewById(R.id.email_login_edit);
        password=findViewById(R.id.password_login_edit);
         loginbutton=findViewById(R.id.btnlogin);
        mAuth = FirebaseAuth.getInstance();
         loginbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
            String emailString,passwordString;
            emailString=email.getText().toString();
            passwordString=password.getText().toString();
            if (emailString.isEmpty())
            {
                email.setError("Email required*");
            }
            else if(passwordString.isEmpty())
            {
                password.setError("password*");
            }
            else if(passwordString.length()<8)
            {
                password.setError("Password must greater then 8 words");
            }
            else
            {
                try {

                    mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginScreen.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(LoginScreen.this,DashBoard.class);
                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onCanceled: "+e.toString());
                                    Toast.makeText(LoginScreen.this,""+e.toString(), Toast.LENGTH_LONG).show();

                                }
                            })
                            .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                            Toast.makeText(LoginScreen.this,"Email and Password not correct", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(LoginScreen.this, e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
             }
         });
    }

    public void conCikeCreateAcount(View view) {
        Intent intent =new Intent(LoginScreen.this,SignUp.class);
        finish();
        startActivity(intent);
    }

    public void OnPasswordClick(View view) {
        Intent intent =new Intent(LoginScreen.this,ForgetPassword.class);
        finish();
        startActivity(intent);
    }
}
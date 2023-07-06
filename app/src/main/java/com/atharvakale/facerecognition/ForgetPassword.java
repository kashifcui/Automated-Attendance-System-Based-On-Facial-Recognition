package com.atharvakale.facerecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
 EditText email;
 AppCompatButton sendpassButton;
 FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email=findViewById(R.id.email_send_password);
        mAuth=FirebaseAuth.getInstance();
        sendpassButton=findViewById(R.id.send_passd_button);
        sendpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString=email.getText().toString();
                if (emailString.isEmpty())
                {
                    email.setError("*Email");
                }
                else {
                    mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ForgetPassword.this, "Password Reset Link send Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });
    }
}
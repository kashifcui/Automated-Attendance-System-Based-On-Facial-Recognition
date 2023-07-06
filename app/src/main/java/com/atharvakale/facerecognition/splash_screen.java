package com.atharvakale.facerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class splash_screen extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(splash_screen.this, DashBoard.class);
//                finish();
//                startActivity(intent);
                if(mAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(splash_screen.this, LoginScreen.class);
                    finish();
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(splash_screen.this, DashBoard.class);
                    finish();
                    startActivity(intent);
                }
            }
        },2500);
    }
}
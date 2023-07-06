package com.atharvakale.facerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.atharvakale.facerecognition.AdopterClass.ClassAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashBoard extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.action_home:
                    selectedFragment = new Attandenss_Screen_Fragment();
                    break;
                case R.id.action_search:
                    selectedFragment = new ProfileFragment();
//                    selectedFragment = new Notificaion();
                    break;
                case R.id.action_profile:

                    Intent intent=new Intent(this,RemindersActivity.class);
                    startActivity(intent);
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        // Set the default fragment to be displayed when the activity starts
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
    }

package com.atharvakale.facerecognition;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    ImageView priviceyimage,aboutimage,profileimage,logoutimage;
    FirebaseAuth mauth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
            priviceyimage=view.findViewById(R.id.priviceyscreengo);
            aboutimage=view.findViewById(R.id.aboutid);
            profileimage=view.findViewById(R.id.profileid);
        logoutimage=view.findViewById(R.id.logoutid);
        mauth = FirebaseAuth.getInstance();
        priviceyimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),  priviseyScreen.class);
                startActivity(intent);
            }
        });
        aboutimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),  AboutScreen.class);
                startActivity(intent);
            }
        });
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),  ProfileScreen.class);
                startActivity(intent);
            }
        });
        logoutimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                Intent intent=new Intent(getContext(),  LoginScreen.class);
                startActivity(intent);

            }
        });
        return  view;
    }
}
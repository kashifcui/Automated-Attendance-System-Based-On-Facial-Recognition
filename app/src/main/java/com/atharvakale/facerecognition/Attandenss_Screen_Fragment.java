package com.atharvakale.facerecognition;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Attandenss_Screen_Fragment extends Fragment {
    CardView classCard,CourseCard,notificationcard,settingcard;
    TextView uname;
    FirebaseUser mauth;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_attandenss__screen_, container, false);
        classCard=view.findViewById(R.id.attanece_home_card);
                CourseCard=view.findViewById(R.id.OurCourses_home_card);
                notificationcard=view.findViewById(R.id.notificationcard);
        settingcard=view.findViewById(R.id.settingcard);
        uname = view.findViewById(R.id.textView22);
        mauth = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("profile").document(mauth.getUid()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Retrieve the value of a specific field
                            String name = documentSnapshot.getString("name");
//                            String email = documentSnapshot.getString("email");
                            uname.setText(name);
//                            uemail.setText(email);
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
        notificationcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RemindersActivity.class);
                startActivity(intent);
            }
        });
        settingcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragmentTransaction=new ProfileFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, fragmentTransaction);
                fragmentTransaction2.commit();   }
        });
                classCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ClassDetailActivity.class);
                        intent.putExtra("active","recognize");
//        finish();
                        startActivity(intent);
                    }
                });
                CourseCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ClassDetailActivity.class);
                        intent.putExtra("active","add");
//        finish();
                        startActivity(intent);
                    }
                });
       return  view;
    }
//    public void attancessCarsClic(View view) {
//        Intent intent = new Intent(getContext(), ClassDetailActivity.class);
//        intent.putExtra("active","recognize");
////        finish();
//        startActivity(intent);
//    }
//    public void CoursesCardClick(View view) {
//        Intent intent = new Intent(getContext(), ClassDetailActivity.class);
//        intent.putExtra("active","add");
////        finish();
//        startActivity(intent);
//    }
}
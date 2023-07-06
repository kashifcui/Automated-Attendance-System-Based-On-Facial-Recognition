package com.atharvakale.facerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.atharvakale.facerecognition.AdopterClass.ViewAttandenceAdapter;
import com.atharvakale.facerecognition.data.model.ViewAttandnessModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendnessActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    private RecyclerView.LayoutManager listLayoutManager;
    List<ViewAttandnessModel> courseList = new ArrayList<>();
    final Calendar myCalendar= Calendar.getInstance();
    String className,courseName,courseId,screenname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendness);
        recyclerView=findViewById(R.id.attance_show_recyclerview);
        className = getIntent().getStringExtra("myclass");
        courseId = getIntent().getStringExtra("courseid");
        courseName=getIntent().getStringExtra("course");
        screenname=getIntent().getStringExtra("screen");
        firestore = FirebaseFirestore.getInstance();
        firebaseDatashiw();
    }
    private void firebaseDatashiw() {
        try {


            firestore.collection("UserData").document(className).collection(courseName)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            // Access the document ID
                            if (documentSnapshot.exists()) {
//                            String datedatabase = documentSnapshot.getString("date");
//                            if (datedatabase.contains(dateget)) {
                                String doucID = documentSnapshot.getId();
                                String calssName = documentSnapshot.getString("className");
                                String courseId = documentSnapshot.getString("course");
//                                String date = documentSnapshot.getString("date");
                                String name = documentSnapshot.getString("id");
                                String rollno = documentSnapshot.getString("rollno");
//                                String time = documentSnapshot.getString("time");

//                                ViewAttandnessModel classModel = new ViewAttandnessModel(
//                                        rollno,
//                                        name,
//                                        calssName
//                                );
//                                Toast.makeText(this, "data get", Toast.LENGTH_SHORT).show();
                                courseList.add(new ViewAttandnessModel(rollno, name, calssName, doucID, courseId,screenname));
                          /*  recyclerList.add(hashMap);
                            setupRecycler();*/
//                            if (className.equals(classSt) && rollno.equals(courseSt)) {

                            }
                        }
//                        Toast.makeText(this, ""+courseList.size(), Toast.LENGTH_SHORT).show();
                        listLayoutManager = new LinearLayoutManager(AttendnessActivity.this);
                        ViewAttandenceAdapter adapter = new ViewAttandenceAdapter(courseList, this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(listLayoutManager);
                        adapter.notifyDataSetChanged();

                        // Pass the retrieved data to the callback
//                    }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure case
//                    callback.onDataLoadFailed(e);
                        System.out.println("Failed to retrieve documents from collection: " + e.getMessage());
                    });
        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
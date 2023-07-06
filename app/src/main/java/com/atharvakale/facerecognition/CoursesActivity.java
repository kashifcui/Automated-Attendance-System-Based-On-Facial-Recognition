package com.atharvakale.facerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharvakale.facerecognition.AdopterClass.ClassAdapter;
import com.atharvakale.facerecognition.AdopterClass.CourseAdapter;
import com.atharvakale.facerecognition.ModelClasses.ClassModel;
import com.atharvakale.facerecognition.ModelClasses.Course;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String className,active;
    FloatingActionButton fbtn;
    FirebaseAuth mAuth;
    private RecyclerView.LayoutManager listLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        className = getIntent().getStringExtra("myclass");
        active = getIntent().getStringExtra("active");
        mAuth=FirebaseAuth.getInstance();
        getData();
        fbtn=findViewById(R.id.addnewCourse);
        if (active.contains("recognize"))
        {
            fbtn.setVisibility(View.GONE);
        }
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showDialog(className);
            }
        });

    }
   private void getData(){
       RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
       List<Course> courseList = new ArrayList<>();

       db = FirebaseFirestore.getInstance();
       CollectionReference courseRef = db.collection("course");
       recyclerView.setLayoutManager(new LinearLayoutManager(this));

       courseRef.whereEqualTo("className",className).get()
               .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       for (DocumentSnapshot document : task.getResult()) {
                           String uid = document.getString("userId");
                           if (uid.contains(mAuth.getUid())) {
                               String CourseDoId=document.getId();
                               String courseName = document.getString("courseName");
                               String courseId = document.getString("courseId");
                               Course classModel = new Course(courseName, courseId, className, active,CourseDoId);
//                           Toast.makeText(this,"data get", Toast.LENGTH_SHORT).show();
                               courseList.add(classModel);
                           }
                       }
                       CourseAdapter adapter = new CourseAdapter(courseList,this);
                       listLayoutManager = new LinearLayoutManager(CoursesActivity.this);

                       recyclerView.setAdapter(adapter);
                       recyclerView.setLayoutManager(listLayoutManager);
                       adapter.notifyDataSetChanged();
                       // Use the classList as needed (e.g., populate RecyclerView)
                   } else {
                       // Handle error
                   }
               });
   }
    Map<String, Object> courseList;
    private void showDialog(String className) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);
        db = FirebaseFirestore.getInstance();
        EditText courseId = dialogView.findViewById(R.id.courseid_edit_dialog);
        EditText courseName = dialogView.findViewById(R.id.coursename_edit_dialog);

        dialogBuilder.setTitle("Save Course")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String courseid = courseId.getText().toString();
                        String coursename = courseName.getText().toString();
                        courseList = new HashMap<>();
                        if (courseid.isEmpty()){
                            courseId.setError("*Course Id");
                        }
                        else if(coursename.isEmpty())
                        {
                            courseName.setError("Course Name");
                        }
                        else{
                            courseList.put("courseId", courseid);
                            courseList.put("courseName", coursename);
                            courseList.put("className", className);
                            courseList.put("userId",mAuth.getCurrentUser().getUid());
                            db.collection("course").add(courseList);
                            getData();
                        }
                        // Handle the data entered by the user
                        // e.g., validate, save, or process the data

                        // You can also pass the data to another method or activity
                        // by using Intent or callbacks, depending on your requirement
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
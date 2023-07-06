package com.atharvakale.facerecognition;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atharvakale.facerecognition.AdopterClass.ClassAdapter;
import com.atharvakale.facerecognition.ModelClasses.ClassModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDetailActivity extends AppCompatActivity {
   ImageView addicon;
    FirebaseFirestore db;
    String active;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager listLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        mAuth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
        active=intent.getStringExtra("active");

        addicon=findViewById(R.id.addiconImage);
        if (active.contains("recognize"))
        {
            addicon.setVisibility(View.GONE);
        }
         recyclerView = findViewById(R.id.classRecylerview);

        List<ClassModel> courseList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
            getFirebaseData();

        //                ClassAdapter adapter = new ClassAdapter(courseList);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
        addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }
    Map<String, Object> courseList;
    private void showDialog() {
        db = FirebaseFirestore.getInstance();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_class_add, null);
        dialogBuilder.setView(dialogView);

        EditText courseId = dialogView.findViewById(R.id.classid_edit_dialog);

        dialogBuilder.setTitle("add")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String courseid = courseId.getText().toString();
                        courseList = new HashMap<>();
                        if (courseid.isEmpty()){
                            courseId.setError("*Class name");
                        }
//                        else if(coursename.isEmpty())
//                        {
//                            courseName.setError("Course Name");
//                        }
                        else{
                            courseList.put("className", courseid);
                            courseList.put("userId", mAuth.getCurrentUser().getUid());
                            db.collection("classes").add(courseList);
                            getFirebaseData();
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
   private void getFirebaseData()
    {
        CollectionReference courseRef = db.collection("classes");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ClassModel> classList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("classes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            String userId = document.getString("userId");
                            if (userId.contains(mAuth.getUid())) {
                                String className = document.getString("className");
                                String id=document.getId();

//                            ClassModel classModel = new ClassModel(className);
                                classList.add(new ClassModel(className, active,id));
                            }
                        }
//                        Toast.makeText(this, ""+classList.size(), Toast.LENGTH_SHORT).show();
                        ClassAdapter adapter = new ClassAdapter(classList,this);
                        listLayoutManager = new LinearLayoutManager(ClassDetailActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(listLayoutManager);
                        adapter.notifyDataSetChanged();

                        // Use the classList as needed (e.g., populate RecyclerView)
                    } else {
                        // Handle error
                    }

                });
    }
}
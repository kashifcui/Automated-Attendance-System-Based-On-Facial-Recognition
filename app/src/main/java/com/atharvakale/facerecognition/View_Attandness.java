package com.atharvakale.facerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvakale.facerecognition.AdopterClass.CourseAdapter;
import com.atharvakale.facerecognition.AdopterClass.ViewAttandenceAdapter;
import com.atharvakale.facerecognition.ModelClasses.Course;
import com.atharvakale.facerecognition.data.model.ViewAttandnessModel;
import com.atharvakale.facerecognition.listener.SimilarityClassifier;
import com.atharvakale.facerecognition.moduleclasses.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class View_Attandness extends AppCompatActivity {
    TextView datetextfied;
    String dateget;
    FirebaseFirestore firestore;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    String courseSt,classSt,courid,screenname;
    private RecyclerView.LayoutManager listLayoutManager;
    List<ViewAttandnessModel> courseList = new ArrayList<>();
    final Calendar myCalendar= Calendar.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attandness);
        datetextfied = (TextView) findViewById(R.id.datepickfield);
        recyclerView=findViewById(R.id.attance_show_recyclerview);
        firestore = FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        courseSt=intent.getStringExtra("course");
        classSt=intent.getStringExtra("myclass");
        courid=intent.getStringExtra("courseid");
        screenname=getIntent().getStringExtra("screen");
//        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH,month);
//                myCalendar.set(Calendar.DAY_OF_MONTH,day);
////                updateLabel();
//                PickDate();
//            }
//        };
        datetextfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickDate();
//                new DatePickerDialog(View_Attandness.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//        Toast.makeText(this, ""+dateget, Toast.LENGTH_SHORT).show();



    }
//    private void updateLabel(){
//        String myFormat="yyyy-M-d";
//        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
//        dateget=dateFormat.format(myCalendar.getTime());
//        datetextfied.setText(dateFormat.format(myCalendar.getTime()));
//        firebaseDatashiw();
//    }
public void PickDate() {
        courseList.clear();
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);


    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mDay = dayOfMonth;
                    mMonth = monthOfYear;
                    mYear = year;

                    dateget=mYear+"-"+mMonth+"-"+mDay;
                    datetextfied.setText(dateget);
                    firebaseDatashiw();
//                    timePicker();
//                    Toast.makeText(context, "Year Select:" + mYear + "Month Select: " + mMonth, Toast.LENGTH_SHORT).show();
//                     txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                }
            }, mYear, mMonth, mDay);
    datePickerDialog.show();
//     Toast.makeText(context, "Year Select:"+mYear+"Month Select: "+mMonth, Toast.LENGTH_SHORT).show();
}

    private void firebaseDatashiw() {
        try {
            firestore.collection("attandenseMark").document(classSt).collection(courseSt)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            // Access the document ID
                            if (documentSnapshot.exists()) {
                                String datedatabase = documentSnapshot.getString("date");
                                if (datedatabase.contains(dateget)) {
                                    String id = documentSnapshot.getId();
                                    String calssName = documentSnapshot.getString("calssName");
                                    String courseId = documentSnapshot.getString("courseId");
//                                String date = documentSnapshot.getString("date");
                                    String name = documentSnapshot.getString("name");
                                    String rollno = documentSnapshot.getString("rollno");
                                    String time = documentSnapshot.getString("time");

//                                ViewAttandnessModel classModel = new ViewAttandnessModel(
//                                        rollno,
//                                        name,
//                                        calssName
//                                );
//                                    Toast.makeText(this, "data get", Toast.LENGTH_SHORT).show();
                                    courseList.add(new ViewAttandnessModel(rollno, name, calssName, id, courseId,screenname));
                          /*  recyclerList.add(hashMap);
                            setupRecycler();*/
//                            if (className.equals(classSt) && rollno.equals(courseSt)) {

                                }
                            }
//                            Toast.makeText(this, "" + courseList.size(), Toast.LENGTH_SHORT).show();
                            listLayoutManager = new LinearLayoutManager(View_Attandness.this);
                            ViewAttandenceAdapter adapter = new ViewAttandenceAdapter(courseList, this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(listLayoutManager);
                            adapter.notifyDataSetChanged();

                            // Pass the retrieved data to the callback
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure case
//                    callback.onDataLoadFailed(e);
                        System.out.println("Failed to retrieve documents from collection: " + e.getMessage());
                    });
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
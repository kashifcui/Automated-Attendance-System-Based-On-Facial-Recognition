package com.atharvakale.facerecognition;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atharvakale.facerecognition.AdopterClass.ViewAttandenceAdapter;
import com.atharvakale.facerecognition.R;
import com.atharvakale.facerecognition.data.model.ViewAttandnessModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class downloadAttandness extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 1234;

    private Button btnDownload;
    String className, courseName, courseId,screenname;
    private FirebaseFirestore firestore;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private RecyclerView.LayoutManager listLayoutManager;
    DatePickerDialog datePickerDialog;
    TextView datetextfied;
    final Calendar myCalendar= Calendar.getInstance();
    RecyclerView recyclerView;
    String dateget;
    List<ViewAttandnessModel> courseList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_attandness);

        firestore = FirebaseFirestore.getInstance();
        className = getIntent().getStringExtra("myclass");
        courseId = getIntent().getStringExtra("courseid");
        courseName = getIntent().getStringExtra("course");
        screenname=getIntent().getStringExtra("screen");
        datetextfied = (TextView) findViewById(R.id.datepickfield);
        recyclerView=findViewById(R.id.attance_show_recyclerview);
        // Request write storage permission if not granted
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setVisibility(View.GONE);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    exportDataToExcel();
                } else {
                    requestPermissions();
                }
            }
        });

        createNotificationChannel();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH,month);
//                myCalendar.set(Calendar.DAY_OF_MONTH,day);
//                updateLabel();
//            }
//        };
        datetextfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickDate();
//                new DatePickerDialog(downloadAttandness.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
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
    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                exportDataToExcel();
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void exportDataToExcel() {
        // Firestore query
        Task<QuerySnapshot> task = firestore.collection("attandenseMark")
                .document(className).collection(courseName)
                .get();
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                QuerySnapshot querySnapshot = task1.getResult();
                if (querySnapshot != null) {

                    try {
                        // Create CSV data
                        List<String[]> data = new ArrayList<>();

                        // Add header row
                        String[] header = {"Roll No", "Student Name", "Class", "Course", "Attends Time", "Date"};
                        data.add(header);

                        // Add data rows from Firestore documents
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            if (document.getString("date").contains(dateget)) {
                                String[] row = {
                                        document.getString("rollno"),
                                        document.getString("name"),
                                        document.getString("calssName"),
                                        document.getString("courseId"),
                                        document.getString("time"),
                                        document.getString("date")
                                };
                                data.add(row);
                            }
                        }
                        // Get the "Downloads" directory
                        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        if (!downloadsDir.exists()) {
                            downloadsDir.mkdirs();
                        }
                        final int random = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80]
                        // Create the file
                        File file = new File(downloadsDir, String.valueOf(random)+"data.csv");

                        // Write data to the file
                        FileWriter fileWriter = new FileWriter(file);
                        CSVWriter csvWriter = new CSVWriter(fileWriter);
                        csvWriter.writeAll(data);
                        csvWriter.close();

                        // Trigger media scan to make the file visible in downloads
                        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);

                        // Show download completed notification
                        createNotificationChannel();
                        showDownloadCompleteNotification();

                        Toast.makeText(this, "Data exported to data.csv", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(this, "No Record", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Firestore", "Error getting documents: " + task1.getException());
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                CharSequence name = "Download Channel";
                String description = "Channel for download notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showDownloadCompleteNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_download_24)
                    .setContentTitle("Download Complete")
                    .setContentText("Data.csv downloaded successfully.")
                    .setAutoCancel(true);

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
//    private void updateLabel(){
//        String myFormat="yyyy-MM-dd";
//        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
//        dateget=dateFormat.format(myCalendar.getTime());
//        datetextfied.setText(dateFormat.format(myCalendar.getTime()));
//
//        firebaseDatashiw();
//    }
    private void firebaseDatashiw() {
        try {
            firestore.collection("attandenseMark").document(className).collection(courseName)
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
//                                Toast.makeText(this, "data get", Toast.LENGTH_SHORT).show();
                                    courseList.add(new ViewAttandnessModel(rollno, name, calssName, id, courseId,screenname));
                                    if (courseList.size() > 0) {
                                        btnDownload.setVisibility(View.VISIBLE);
                                    }
                                    if (courseList.size()<=0){
                                        btnDownload.setVisibility(View.GONE);
                                    }
                          /*  recyclerList.add(hashMap);
                            setupRecycler();*/
//                            if (className.equals(classSt) && rollno.equals(courseSt)) {

                                }
                            }
//                            Toast.makeText(this, "" + courseList.size(), Toast.LENGTH_SHORT).show();
                            listLayoutManager = new LinearLayoutManager(downloadAttandness.this);
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
        } catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
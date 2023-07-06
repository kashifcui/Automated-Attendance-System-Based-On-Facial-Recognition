package com.atharvakale.facerecognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.atharvakale.facerecognition.moduleclasses.MainActivity;
import com.atharvakale.facerecognition.moduleclasses.RegisterFaceActivity;

public class AttandnessDashBoard extends AppCompatActivity {
    String className,courseName,courseId;
    CardView showattancecard,viewAttanceCard,downloadAtanceCard,addAttanceCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandness_dash_board);
        className = getIntent().getStringExtra("myclass");
        courseId = getIntent().getStringExtra("courseid");
        courseName=getIntent().getStringExtra("course");
        showattancecard=findViewById(R.id.showattance_card_id);
        viewAttanceCard=findViewById(R.id.view_Attandence_id);
        downloadAtanceCard=findViewById(R.id.download_attandes_id);
        addAttanceCard=findViewById(R.id.addAttaness_id);
//        Toast.makeText(this, "Course:"+className, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "class:"+className, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "couse id::"+courseId, Toast.LENGTH_SHORT).show();
        showattancecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AttandnessDashBoard.this,AttendnessActivity.class);
                i.putExtra("myclass", className);
                i.putExtra("course", courseName);
                i.putExtra("courseid", courseId);
                i.putExtra("screen", "showattancecard");
                startActivity(i);
            }
        });
        viewAttanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i=new Intent(AttandnessDashBoard.this,View_Attandness.class);
                i.putExtra("myclass", className);
                i.putExtra("course", courseName);
                i.putExtra("courseid", courseId);
                i.putExtra("screen", "viewAttanceCard");
            startActivity(i);
            }
        });
        downloadAtanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AttandnessDashBoard.this,downloadAttandness.class);
                i.putExtra("myclass", className);
                i.putExtra("course", courseName);
                i.putExtra("courseid", courseId);
                i.putExtra("screen", "downloadAtanceCard");
                startActivity(i);
            }
        });
        addAttanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttandnessDashBoard.this, MainActivity.class);
                intent.putExtra("myclass", className);
                intent.putExtra("course", courseName);
                intent.putExtra("courseid", courseId);
                intent.putExtra("screen", "addAttanceCard");
                startActivity(intent);
            }
        });
    }
}
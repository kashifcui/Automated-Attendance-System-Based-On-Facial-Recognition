package com.atharvakale.facerecognition.AdopterClass;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atharvakale.facerecognition.CoursesActivity;
import com.atharvakale.facerecognition.ModelClasses.ClassModel;
import com.atharvakale.facerecognition.R;
import com.atharvakale.facerecognition.data.model.ViewAttandnessModel;
import com.atharvakale.facerecognition.listener.SimilarityClassifier;
import com.atharvakale.facerecognition.moduleclasses.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAttandenceAdapter extends RecyclerView.Adapter<ViewAttandenceAdapter.CourseViewHolder> {
    private List<ViewAttandnessModel> courseList;
    private FirebaseFirestore db;
    private Context context;
    private static final String TAG = "ViewAttandenceAdapter";
    Map<String, Object> updateList;
    public ViewAttandenceAdapter(List<ViewAttandnessModel> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attanceshowrecyclerview, parent, false);
        return new CourseViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        ViewAttandnessModel classModel = courseList.get(position);
        holder.nameTextView.setText(classModel.getStName());
        holder.className.setText(classModel.getStClass());
        holder.rollno.setText(classModel.getRollNo());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classModel.getsCreen().equals("showattancecard")) {
                    deleteItem(position, classModel.getStCourse(), classModel.getStClass());
                }
                if (classModel.getsCreen().equals("viewAttanceCard")||classModel.getsCreen().equals("downloadAtanceCard"))
                {

                    deleteItemSecondtwo(position, classModel.getStCourse(), classModel.getStClass());

                }
           }
        });
        holder.editeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 updateList=new HashMap<>();;
//                    start = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Enter Fields");
                    View view = LayoutInflater.from(context).inflate(R.layout.register_layout, null);
                    TextInputEditText name, rollno, className,course;
                    name = view.findViewById(R.id.name);
                    rollno = view.findViewById(R.id.st_roll);
                    course = view.findViewById(R.id.rollno);
                    course.setVisibility(View.GONE);

                    className = view.findViewById(R.id.className);
                    className.setVisibility(View.GONE);
//                    course.setText(classModel.getStCourse());
//                    className.setText(classModel.getStClass());
                rollno.setText(classModel.getRollNo());
                name.setText(classModel.getStName());
                    builder.setView(view);

                    // Set up the buttons
                    builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(context, input.getText().toString(), Toast.LENGTH_SHORT).show();

                            //Create and Initialize new object with Face embeddings and Name.
//                            String extra = Utils.convertToString(embeedings);
                            if (rollno.getText().toString().isEmpty()) {
//                                rollno.setError("Enter Roll No");

                            }
                            else {
                                updateList.put("rollno",(rollno.getText().toString()));
                            }
                             if (name.getText().toString().isEmpty()) {
//                                name.setError("Enter Name");
                            }
                             else
                             {
                                 updateList.put("name",(name.getText().toString()));
                             }
                        if (classModel.getsCreen().equals("showattancecard")) {
                            updateData(classModel.getGetUid(), classModel.getStClass(), classModel.getStCourse());
                        }
                            if (classModel.getsCreen().equals("viewAttanceCard")||classModel.getsCreen().equals("downloadAtanceCard")) {
                                updateDataViewAttance(classModel.getGetUid(), classModel.getStClass(), classModel.getStCourse());
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            start = true;
                            dialog.cancel();
                        }
                    });

                    builder.show();

//                updateData(classModel.getGetUid(),classModel.getStName(),classModel.getRollNo(),classModel.getStClass(),classModel.getStCourse());
            }
        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CoursesActivity.class);
////                intent.putExtra("myclass", classModel.getClassName());
////                intent.putExtra("active", classModel.getActive());
//                context.startActivity(intent);
//            }
//        });
//        holder.idTextView.setText(course.getId());
    }
    public void deleteItemSecondtwo(int position,String clasname,String coursename) {
        ViewAttandnessModel item = courseList.get(position);
        String itemId = item.getGetUid();
        db = FirebaseFirestore.getInstance();
        try {
            db.collection("attandenseMark")
                    .document(coursename).collection(clasname).document(itemId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            courseList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, courseList.size());
                            Toast.makeText(context, "Data Deleted Sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteItem(int position,String clasname,String coursename) {
        ViewAttandnessModel item = courseList.get(position);
        String itemId = item.getGetUid();
        db = FirebaseFirestore.getInstance();
        try {
            db.collection("UserData")
                    .document(coursename).collection(clasname).document(itemId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            courseList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, courseList.size());
                            Toast.makeText(context, "Data Deleted Sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void updateDataViewAttance(String documentId,String clasname,String coursename) {
        Log.d(TAG, "AddFace:clasname" +clasname);
        Log.d(TAG, "AddFace:coursename" +coursename);
        Log.d(TAG, "AddFace:documentId" +documentId);
        Log.d(TAG, "AddFace:name is" );
        db = FirebaseFirestore.getInstance();
        db.collection("attandenseMark").document(clasname).collection(coursename).document(documentId)
                .update(updateList).addOnSuccessListener(aVoid -> {
//                    notifyItemRangeChanged(position, courseList.size());
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                    // Update successful
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "AddFace:name" +e.getMessage());

                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    // Handle error
                });
    }
    private void updateData(String documentId,String clasname,String coursename) {
        Log.d(TAG, "AddFace:clasname" +clasname);
        Log.d(TAG, "AddFace:coursename" +coursename);
        Log.d(TAG, "AddFace:documentId" +documentId);
        Log.d(TAG, "AddFace:name is" );
        db = FirebaseFirestore.getInstance();
        db.collection("UserData").document(clasname).collection(coursename).document(documentId)
        .update(updateList).addOnSuccessListener(aVoid -> {
//                    notifyItemRangeChanged(position, courseList.size());
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                    // Update successful
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "AddFace:name" +e.getMessage());

                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    // Handle error
                });
    }
    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView rollno;
        ImageView editeButton,deleteButton;
        TextView className;
//        TextView idTextView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            rollno=itemView.findViewById(R.id.roll_no);
            className=itemView.findViewById(R.id.class_attand);
            nameTextView = itemView.findViewById(R.id.name_view);
            editeButton=itemView.findViewById(R.id.edit_btn);
            deleteButton=itemView.findViewById(R.id.delecte_btn);

        }
    }
}

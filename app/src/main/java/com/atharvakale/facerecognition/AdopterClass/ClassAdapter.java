package com.atharvakale.facerecognition.AdopterClass;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atharvakale.facerecognition.CoursesActivity;
import com.atharvakale.facerecognition.DashBoard;
import com.atharvakale.facerecognition.ModelClasses.ClassModel;
import com.atharvakale.facerecognition.ModelClasses.Course;
import com.atharvakale.facerecognition.R;
import com.atharvakale.facerecognition.data.model.ViewAttandnessModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.CourseViewHolder> {
    private List<ClassModel> courseList;
    private   FirebaseAuth mAuth;
    private Context context;
    private FirebaseFirestore db;
    public ClassAdapter(List<ClassModel> courseList,Context context) {
        this.courseList = courseList;
        this.context = context;
    }
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adopter_layout, parent, false);
        return new CourseViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        ClassModel classModel = courseList.get(position);
        holder.nameTextView.setText(classModel.getClassName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CoursesActivity.class);
                intent.putExtra("myclass", classModel.getClassName());
                intent.putExtra("active", classModel.getActive());
                context.startActivity(intent);
            }
        });
        if (classModel.getActive().contains("recognize")) {
            holder.delele.setVisibility(View.GONE);

//            holder.update.setVisibility(View.GONE);

        }
        else
        {
            holder.delele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView delele;

//        TextView idTextView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.className);
            delele=itemView.findViewById(R.id.del);
//            update=itemView.findViewById(R.id.update);
//            idTextView = itemView.findViewById(R.id.courseId);
        }
    }
    public void deleteItem(int position) {
        ClassModel item = courseList.get(position);
        String itemId = item.id;
        db = FirebaseFirestore.getInstance();
        try {
            db.collection("classes").document(itemId)
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
    LayoutInflater inflater;
    Map<String, Object> courseListdata;
    Map<String, Object> courseUpdateListdata;

//    private void showDialogUpdate(String docId,int position,String ClassName) {
//        mAuth=FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//        inflater = LayoutInflater.from(context);
//        View dialogView = inflater.inflate(R.layout.dialog_class_add, null);
//        dialogBuilder.setView(dialogView);
//
//        EditText courseId = dialogView.findViewById(R.id.classid_edit_dialog);
//
//        dialogBuilder.setTitle("Update")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        String courseid = courseId.getText().toString();
//                        courseListdata = new HashMap<>();
//                        if (courseid.isEmpty()) {
//                            courseId.setError("*Updated Name");
//                        }
////                        else if(coursename.isEmpty())
////                        {
////                            courseName.setError("Course Name");
////                        }
//                        else {
//                            courseListdata.put("className", courseid);
//
////                            courseListdata.put("userId", mAuth.getCurrentUser().getUid());
//                            db.collection("classes").document(docId).update(courseListdata).addOnSuccessListener(
//                                    new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            CollectionReference courseRef = db.collection("course");
//
//                                            courseRef.whereEqualTo("className", ClassName).get()
//                                                    .addOnCompleteListener(task -> {
//                                                                if (task.isSuccessful()) {
//                                                                    for (DocumentSnapshot document : task.getResult()) {
//                                                                        String uid = document.getString("userId");
//                                                                        if (uid.contains(mAuth.getUid())) {
//                                                                            String courseDocId=document.getId();
//                                                                            courseRef.document(courseDocId).
//                                                                                    update(courseListdata);
////                                                                            String courseName = document.getString("courseName");
////                                                                            String courseId = document.getString("courseId");
//
//                                                                        }
//                                                                    }
//
//                                                                }
//                                                            }
//                                                    ).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                            notifyItemRemoved(position);
//                                                            notifyItemRangeChanged(position, courseList.size());
//                                                            Intent i=new Intent(context, DashBoard.class);
//                                                            Toast.makeText(context, "Class Update", Toast.LENGTH_SHORT).show();
//                                                           context.startActivity(i);
//                                                        }
//                                                    });
//
//                                        }
//                                        // Handle the data entered by the user
//                                        // e.g., validate, save, or process the data
//
//                                        // You can also pass the data to another method or activity
//                                        // by using Intent or callbacks, depending on your requirement
//                                    });
//                        }
//                    };
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }
}

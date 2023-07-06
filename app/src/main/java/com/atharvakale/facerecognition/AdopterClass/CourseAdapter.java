    package com.atharvakale.facerecognition.AdopterClass;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.atharvakale.facerecognition.AttandnessDashBoard;
    import com.atharvakale.facerecognition.ModelClasses.ClassModel;
    import com.atharvakale.facerecognition.ModelClasses.Course;
    import com.atharvakale.facerecognition.R;
    import com.atharvakale.facerecognition.moduleclasses.MainActivity;
    import com.atharvakale.facerecognition.moduleclasses.RegisterFaceActivity;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.util.List;

    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
        private List<Course> courseList;
        private Context context;
        private FirebaseFirestore db;
        public CourseAdapter(List<Course> courseList, Context context) {
            this.courseList = courseList;
            this.context=context;
        }

        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view_layout, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course course = courseList.get(position);
            holder.nameTextView.setText(course.getName());
            holder.idTextView.setText(course.getId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String active= course.getActive();
                    if (active.equals("recognize")) {
                        Intent intent = new Intent(context, AttandnessDashBoard.class);
//                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("myclass", course.getClasName());
                        intent.putExtra("course", course.getName());
                        intent.putExtra("courseid", course.getId());
                        context.startActivity(intent);
                    }
                    else {

                        Intent intent = new Intent(context, RegisterFaceActivity.class);
                        intent.putExtra("myclass", course.getClasName());
                        intent.putExtra("course", course.getName());
                        intent.putExtra("courseid", course.getId());
                        context.startActivity(intent);
                    }
                }
            });

            if (course.getActive().contains("recognize")) {
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
            TextView idTextView;
            ImageView delele;
            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.courseName);
                idTextView = itemView.findViewById(R.id.courseId);
                delele=itemView.findViewById(R.id.del);
            }
        }
        public void deleteItem(int position) {
            Course item = courseList.get(position);
            String itemId = item.getCourseDocId();
            db = FirebaseFirestore.getInstance();
            try {
                db.collection("course").document(itemId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                courseList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, courseList.size());
                                Toast.makeText(context, "Course Deleted Sucessfully", Toast.LENGTH_SHORT).show();
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
    }
    package com.atharvakale.facerecognition.AdopterClass;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.atharvakale.facerecognition.ModelClasses.Course;
    import com.atharvakale.facerecognition.ModelClasses.Present;
    import com.atharvakale.facerecognition.PresentItemClickListner;
    import com.atharvakale.facerecognition.R;
    import com.atharvakale.facerecognition.moduleclasses.MainActivity;
    import com.atharvakale.facerecognition.moduleclasses.RegisterFaceActivity;

    import java.util.ArrayList;
    import java.util.List;

    public class PresentAdapter extends RecyclerView.Adapter<PresentAdapter.CourseViewHolder> {
        private ArrayList<String> courseList;
        private Context context;
        private final PresentItemClickListner mListner;
        public PresentAdapter(ArrayList<String> courseList, Context context,PresentItemClickListner mListner) {
            this.courseList = courseList;
            this.context=context;
            this.mListner=mListner;
        }

        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.present_item, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
//            Present course = courseList.get(position);
            holder.nameTextView.setText(courseList.get(position));


            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.deleteSingleItem( holder.getAdapterPosition());
                }
            });

        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        public class CourseViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            ImageView del;


            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.name);
                del = itemView.findViewById(R.id.del);

            }
        }
    }
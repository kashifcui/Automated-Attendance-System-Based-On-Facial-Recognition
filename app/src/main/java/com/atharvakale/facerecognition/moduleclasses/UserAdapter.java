package com.atharvakale.facerecognition.moduleclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atharvakale.facerecognition.R;
import com.atharvakale.facerecognition.listener.SimilarityClassifier;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<String> keys;
    private List<HashMap<String, SimilarityClassifier.Recognition>> recyclerList;
    private Context context;
    public UserAdapter(Context context, List<HashMap<String, SimilarityClassifier.Recognition>> recyclerList, List<String> entryKey) {
        this.context = context;
        this.keys = entryKey;
        this.recyclerList = recyclerList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.register_user_item,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        String key = keys.get(position);
        HashMap<String, SimilarityClassifier.Recognition> map = recyclerList.get(position);

        holder.name.setText(map.get(key).getId());
    }

    @Override
    public int getItemCount() {
        return recyclerList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView name;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}

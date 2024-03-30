package com.example.workouttrack.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workouttrack.R;
import com.example.workouttrack.models.WorkoutItem;

import java.util.ArrayList;

public class HorizontalAdapter  extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WorkoutItem> workoutItemList;

    public HorizontalAdapter(Context context,ArrayList<WorkoutItem> workoutItemList) {
        this.workoutItemList = workoutItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = workoutItemList.get(position).getTitle();
        String content = workoutItemList.get(position).getContent();
        String detail = workoutItemList.get(position).getDetail();
        holder.textViewItemContent.setText(content);
        holder.textViewItemTitle.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailsDialog(title,detail);
            }
        });
    }

    private void showDetailsDialog(String title,String detail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(detail);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return workoutItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItemTitle;
        public TextView textViewItemContent;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemContent = itemView.findViewById(R.id.textViewItemContent);
            textViewItemTitle = itemView.findViewById(R.id.textViewItemTitle);
        }
    }
}
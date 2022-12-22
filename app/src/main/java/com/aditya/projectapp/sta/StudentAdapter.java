package com.aditya.projectapp.sta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{

    Context context;
    int lastPosition = -1;
    ArrayList<StudentData> studentDataArrayList;

    public StudentAdapter(Context context, ArrayList<StudentData> studentDataArrayList) {
        this.context = context;
        this.studentDataArrayList = studentDataArrayList;
    }

    public void filteredList(ArrayList<StudentData> filterList1) {
        studentDataArrayList = filterList1;
        notifyDataSetChanged();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{

        ImageView userImageView;
        TextView userNameTextView;
        CardView listCardView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImage);
            userNameTextView = itemView.findViewById(R.id.userName);
            listCardView = itemView.findViewById(R.id.listCardView);
        }
    }


    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyler_row_item, parent, false);
        StudentAdapter.StudentViewHolder studentViewHolder = new StudentAdapter.StudentViewHolder(view);
        return studentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.StudentViewHolder holder, int position) {
        StudentData studentData = studentDataArrayList.get(position);

        //holder.userImageView.setImageResource(studentData.getUserImage());

        Glide.with(context)
                .load(studentDataArrayList.get(position).getUserImage())
                .into(holder.userImageView);
        holder.userNameTextView.setText(studentData.getUserName());
        holder.listCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userName", studentDataArrayList.get(holder.getAdapterPosition()).getUserName());
                intent.putExtra("userImage", studentDataArrayList.get(holder.getAdapterPosition()).getUserImage());
                context.startActivity(intent);
            }
        });

        setAnimation(holder.itemView, position);

    }

    public void setAnimation(View view, int position){
        if(position > lastPosition){
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(1500);
            view.startAnimation(scaleAnimation);
            lastPosition = position;
        }

    }


    @Override
    public int getItemCount() {
        return studentDataArrayList.size();
    }
}

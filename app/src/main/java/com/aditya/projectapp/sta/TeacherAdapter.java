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

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>{

    Context context;
    int lastPosition = -1;
    ArrayList<TeacherData> teacherDataArrayList;


    public TeacherAdapter(Context context, ArrayList<TeacherData> teacherDataArrayList) {
        this.context = context;
        this.teacherDataArrayList = teacherDataArrayList;
    }

    public void filteredList(ArrayList<TeacherData> filterList2) {
        teacherDataArrayList = filterList2;
        notifyDataSetChanged();
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder{

        ImageView userImageView;
        TextView userNameTextView;
        CardView teacherCardView;


        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImage);
            userNameTextView = itemView.findViewById(R.id.userName);
            teacherCardView = itemView.findViewById(R.id.listCardView);
        }
    }


    @NonNull
    @Override
    public TeacherAdapter.TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyler_row_item, parent, false);
        TeacherAdapter.TeacherViewHolder teacherViewHolder = new TeacherAdapter.TeacherViewHolder(view);
        return teacherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TeacherAdapter.TeacherViewHolder holder, int position) {
        TeacherData teacherData = teacherDataArrayList.get(position);

     //   holder.userImageView.setImageResource(teacherData.getUserImage());

        Glide.with(context)
                .load(teacherDataArrayList.get(position).getUserImage())
                .into(holder.userImageView);
        holder.userNameTextView.setText(teacherData.getUserName());
        holder.teacherCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userName", teacherDataArrayList.get(holder.getAdapterPosition()).getUserName());
                intent.putExtra("userImage", teacherDataArrayList.get(holder.getAdapterPosition()).getUserImage());
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
        return teacherDataArrayList.size();
    }
}

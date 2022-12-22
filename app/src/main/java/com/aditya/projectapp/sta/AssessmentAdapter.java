package com.aditya.projectapp.sta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    int lastPosition = -1;
    ArrayList<StudentAssignmentData> studentAssignmentDataArrayList;
    //   ArrayList<GroupData> groupFilterList;
    Context context;

    public void filteredList(ArrayList<StudentAssignmentData> filterList) {
        studentAssignmentDataArrayList = filterList;
        notifyDataSetChanged();
    }


    public static class AssessmentViewHolder extends RecyclerView.ViewHolder{

        ImageView fileTypeImage;
        TextView title;
        TextView description;
        CardView assessmentCardView;
        Button approveButton;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            title = itemView.findViewById(R.id.title);
            assessmentCardView = (CardView) itemView.findViewById(R.id.assessmentCardView);
            description = itemView.findViewById(R.id.description);
            approveButton = itemView.findViewById(R.id.approveButton);

        }
    }

    public AssessmentAdapter(Context context, ArrayList<StudentAssignmentData> studentAssignmentDataArrayList) {
        this.context = context;
        this.studentAssignmentDataArrayList = studentAssignmentDataArrayList;
        // groupFilterList = new ArrayList<>(groupDataArrayList);
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_assessment_row_item, parent, false);
        AssessmentViewHolder assessmentViewHolder = new AssessmentViewHolder(view);
        return assessmentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AssessmentViewHolder holder, int position) {
        final StudentAssignmentData studentAssignmentData = studentAssignmentDataArrayList.get(position);


        if (studentAssignmentData.getFileType().equals("Image")){
            holder.fileTypeImage.setImageResource(R.drawable.imageicon);
        }else{
            holder.fileTypeImage.setImageResource(R.drawable.pdficon);
        }

        holder.title.setText(studentAssignmentData.getStudentName());
        holder.description.setText(studentAssignmentData.getAssignmentDescription());
        holder.assessmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentAssignmentData.getFileType().equals("Image")){
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("Image", studentAssignmentData.getFileUrl());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    intent.putExtra("Pdf", studentAssignmentData.getFileUrl());
                    context.startActivity(intent);
                }
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("StudentAssignments").child(studentAssignmentData.getGroupName()).child(studentAssignmentData.getAssignmentTitle()).child(studentAssignmentData.getStudentUID()).child("approvalStatus").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    }
                });

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
        return studentAssignmentDataArrayList.size();
    }
}

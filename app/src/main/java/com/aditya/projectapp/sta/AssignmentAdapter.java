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


public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    int lastPosition = -1;
    ArrayList<AssignmentData> assignmentDataArrayList;
    //   ArrayList<GroupData> groupFilterList;
    Context context;

    public void filteredList(ArrayList<AssignmentData> filterList) {
        assignmentDataArrayList = filterList;
        notifyDataSetChanged();
    }


    public static class AssignmentViewHolder extends RecyclerView.ViewHolder{

        ImageView fileTypeImage;
        TextView title;
        TextView description;
        CardView assignmentCardView;
        Button deleteButton, assessmentButton;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            title = itemView.findViewById(R.id.title);
            assignmentCardView = (CardView) itemView.findViewById(R.id.assignmentCardView);
            description = itemView.findViewById(R.id.description);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            assessmentButton = itemView.findViewById(R.id.assessmentButton);
        }
    }

    public AssignmentAdapter(Context context, ArrayList<AssignmentData> assignmentDataArrayList) {
        this.context = context;
        this.assignmentDataArrayList = assignmentDataArrayList;
        // groupFilterList = new ArrayList<>(groupDataArrayList);
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_recycler_row_item, parent, false);
        AssignmentViewHolder assignmentViewHolder = new AssignmentViewHolder(view);
        return assignmentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AssignmentViewHolder holder, int position) {
        final AssignmentData assignmentData = assignmentDataArrayList.get(position);


        if (assignmentData.getFileType().equals("Image")){
            holder.fileTypeImage.setImageResource(R.drawable.imageicon);
        }else{
            holder.fileTypeImage.setImageResource(R.drawable.pdficon);
        }

        holder.title.setText(assignmentData.getAssignmentTitle());
        holder.description.setText(assignmentData.getAssignmentDescription());
        holder.assignmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignmentData.getFileType().equals("Image")){
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("Image", assignmentData.getFileUrl());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    intent.putExtra("Pdf", assignmentData.getFileUrl());
                    context.startActivity(intent);
                }
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Do you really want to delete "+assignmentData.getAssignmentTitle()+"?")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("Assignments").child(assignmentData.getTimeStamp()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, assignmentData.getAssignmentTitle() + " deleted" , Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
        holder.assessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssessmentActivity.class);
                intent.putExtra("assignmentTitle", assignmentData.getAssignmentTitle());
                intent.putExtra("groupName", assignmentData.getGroupName());
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
        return assignmentDataArrayList.size();
    }
}

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AssignmentAdapterStudentEnd extends RecyclerView.Adapter<AssignmentAdapterStudentEnd.AssignmentViewHolder> {

    int lastPosition = -1;
    ArrayList<AssignmentData> assignmentDataArrayList;
    Context context;
    DatabaseReference databaseReference1, databaseReference;

    public void filteredList(ArrayList<AssignmentData> filterList) {
        assignmentDataArrayList = filterList;
        notifyDataSetChanged();
    }


    public static class AssignmentViewHolder extends RecyclerView.ViewHolder{

        ImageView fileTypeImage;
        TextView title;
        TextView description;
        CardView assignmentCardView;
        Button handInButton, statusButton;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            title = itemView.findViewById(R.id.title);
            assignmentCardView = (CardView) itemView.findViewById(R.id.assignmentCardView);
            description = itemView.findViewById(R.id.description);
            handInButton = itemView.findViewById(R.id.handInButton);
            statusButton = itemView.findViewById(R.id.statusButton);
        }
    }

    public AssignmentAdapterStudentEnd(Context context, ArrayList<AssignmentData> assignmentDataArrayList) {
        this.context = context;
        this.assignmentDataArrayList = assignmentDataArrayList;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_student_end_recycler_row_item, parent, false);
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
            }
        });
        holder.handInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Pdf", "Image", "Docx"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose a file type that you want to upload");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(context, items[item], Toast.LENGTH_SHORT).show();
                        if(items[item] == "Docx"){
                            new AlertDialog.Builder(context)
                                    .setMessage("STA does not have support for files with extension docx at this moment please tune in for next updates")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }else{
                            Intent intent = new Intent(context, HandInAssignmentActivity.class);
                            intent.putExtra("fileType", items[item]);
                            intent.putExtra("assignmentTitle", assignmentData.getAssignmentTitle());
                            intent.putExtra("groupName", assignmentData.getGroupName());
                            context.startActivity(intent);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        holder.statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference1 = FirebaseDatabase.getInstance().getReference("StudentAssignments").child(assignmentData.getGroupName()).child(assignmentData.getAssignmentTitle()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ValueEventListener valueEventListener = databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot != null){
                            new AlertDialog.Builder(context)
                                .setTitle(" Approval status")
                                .setIcon(R.drawable.ic_status)
                                .setMessage(snapshot.child("approvalStatus").getValue().toString())
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "You can resubmit the assignment if it gets disapproved by the teacher", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                        }else {
                            Toast.makeText(context, "Please submit the assignment first to check the status", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
        return assignmentDataArrayList.size();
    }
}

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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.StudyMaterialViewHolder> {

    int lastPosition = -1;
    ArrayList<StudyMaterialData> studyMaterialDataArrayList;
    //   ArrayList<GroupData> groupFilterList;
    Context context;

    public void filteredList(ArrayList<StudyMaterialData> filterList) {
        studyMaterialDataArrayList = filterList;
        notifyDataSetChanged();
    }


    public static class StudyMaterialViewHolder extends RecyclerView.ViewHolder{

        ImageView fileTypeImage;
        TextView title;
        TextView description;
        CardView studyMaterialCardView;
        Button  deleteStudyMaterialButton;

        public StudyMaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            title = itemView.findViewById(R.id.title);
            studyMaterialCardView = (CardView) itemView.findViewById(R.id.studyMaterialCardView);
            description = itemView.findViewById(R.id.description);
            deleteStudyMaterialButton = itemView.findViewById(R.id.deleteStudyMaterialButton);

        }
    }

    public StudyMaterialAdapter(Context context, ArrayList<StudyMaterialData> studyMaterialDataArrayList) {
        this.context = context;
        this.studyMaterialDataArrayList = studyMaterialDataArrayList;
        // groupFilterList = new ArrayList<>(groupDataArrayList);
    }

    @NonNull
    @Override
    public StudyMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_material_recycler_view, parent, false);
        StudyMaterialViewHolder studyMaterialViewHolder = new StudyMaterialViewHolder(view);
        return studyMaterialViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StudyMaterialViewHolder holder, int position) {
        final StudyMaterialData studyMaterialData = studyMaterialDataArrayList.get(position);


        if (studyMaterialData.getFileType().equals("Image")){
            holder.fileTypeImage.setImageResource(R.drawable.imageicon);
        }else{
            holder.fileTypeImage.setImageResource(R.drawable.pdficon);
        }

        holder.title.setText(studyMaterialData.getStudyMaterialTitle());
        holder.description.setText(studyMaterialData.getStudyMaterialDescription());
        holder.studyMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studyMaterialData.getFileType().equals("Image")){
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("Image", studyMaterialData.getFileUrl());
                   context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    intent.putExtra("Pdf", studyMaterialData.getFileUrl());
                    context.startActivity(intent);
                }
                    //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.deleteStudyMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Do you really want to delete "+studyMaterialData.getStudyMaterialTitle()+"?")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("StudyMaterial").child(studyMaterialData.getTimeStamp()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, studyMaterialData.getStudyMaterialTitle() + " deleted" , Toast.LENGTH_SHORT).show();
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
        return studyMaterialDataArrayList.size();
    }
}

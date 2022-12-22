package com.aditya.projectapp.sta;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Aditya on 9/22/2020.
 */

public class InstituteAdapter extends RecyclerView.Adapter<InstituteHolder>{

    private Context context;
    private ArrayList<InstituteData> instituteList;
    String selectedInstituteName = "";


    public InstituteAdapter(Context context, ArrayList<InstituteData> instituteList) {
        this.context = context;
        this.instituteList = instituteList;
    }

    @Override
    public InstituteHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_row_item, viewGroup, false);

        return new InstituteHolder(view);
    }

    @Override
    public void onBindViewHolder(final InstituteHolder instituteHolder, final int position) {

        Glide.with(context)
                .load(instituteList.get(position).getItemImage())
                .into(instituteHolder.instituteImageView);
        //instituteHolder.instituteImageView.setImageResource(instituteList.get(position).getItemImage());
        instituteHolder.instituteTitle.setText(instituteList.get(position).getItemName());
        instituteHolder.instituteDescription.setText(instituteList.get(position).getItemDescription());
        instituteHolder.instituteDomain.setText(instituteList.get(position).getItemDomain());
        instituteHolder.enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedInstituteName = instituteList.get(instituteHolder.getAdapterPosition()).getItemName().trim();
                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userInstitute").setValue(selectedInstituteName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Selection successful!", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(context, LogInActivity.class);
                        context.startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Selection failed please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        instituteHolder.instituteCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InstituteDescriptionActivity.class);
                intent.putExtra("Image", instituteList.get(instituteHolder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description", instituteList.get(instituteHolder.getAdapterPosition()).getItemDescription());
                intent.putExtra("Title",instituteList.get(instituteHolder.getAdapterPosition()).getItemName());
                intent.putExtra("URL", instituteList.get(instituteHolder.getAdapterPosition()).getItemDomain());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return instituteList.size();
    }

    public void filteredList(ArrayList<InstituteData> filterList) {
        instituteList = filterList;
        notifyDataSetChanged();
    }
}

class InstituteHolder extends RecyclerView.ViewHolder{

     ImageView instituteImageView;
    TextView instituteTitle, instituteDescription, instituteDomain;
    CardView instituteCardView;
    Button enrollButton;

    public InstituteHolder(View itemView) {
        super(itemView);

        enrollButton = itemView.findViewById(R.id.enrollButton);
        instituteImageView = itemView.findViewById(R.id.instituteImage);
        instituteTitle = itemView.findViewById(R.id.instituteName);
        instituteDescription = itemView.findViewById(R.id.instituteDescription);
        instituteDomain = itemView.findViewById(R.id.instituteDomain);
        instituteCardView = itemView.findViewById(R.id.instituteCardView);
    }
}

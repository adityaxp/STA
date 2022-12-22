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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SelectGroupAdapter extends RecyclerView.Adapter<SelectGroupAdapter.GroupViewHolder> {

    int lastPosition = -1;
    ArrayList<GroupData> groupDataArrayList;
    //   ArrayList<GroupData> groupFilterList;
    Context context;

    public void filteredList(ArrayList<GroupData> filterList) {
        groupDataArrayList = filterList;
        notifyDataSetChanged();
    }


//    @Override
//    public Filter getFilter() {
//        return groupFilter;
//    }
//
//    private Filter groupFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<GroupData> filteredList = new ArrayList<>();
//            if(constraint == null || constraint.length() == 0){
//                filteredList.addAll(groupFilterList);
//            }else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for(GroupData item: groupFilterList){
//                    if(item.getGroupName().toLowerCase().contains(filterPattern) || item.getRecentMessage().toLowerCase().contains(filterPattern)){
//                        filteredList.add(item);
//                    }
//
//                }
//
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            groupDataArrayList.clear();
//            groupDataArrayList.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };


    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        ImageView groupImageView;
        TextView groupNameTextView;
        TextView recentMessageTextView;
        CardView groupCardView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImageView = itemView.findViewById(R.id.groupImage);
            groupNameTextView = itemView.findViewById(R.id.groupName);
            groupCardView = (CardView) itemView.findViewById(R.id.groupCardView);
            recentMessageTextView = itemView.findViewById(R.id.recentMessage);
        }
    }

    public SelectGroupAdapter(Context context, ArrayList<GroupData> groupDataArrayList) {
        this.context = context;
        this.groupDataArrayList = groupDataArrayList;
        // groupFilterList = new ArrayList<>(groupDataArrayList);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_recycle_row_item, parent, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder holder, int position) {
        final GroupData groupData = groupDataArrayList.get(position);


        //    holder.groupImageView.setImageResource(groupData.getGroupImage());

        Glide.with(context)
                .load(groupDataArrayList.get(position).getGroupImage())
                .into(holder.groupImageView);
        holder.groupNameTextView.setText(groupData.getGroupName());
        holder.recentMessageTextView.setText("Teacher: " + groupData.getCreatedBy());
        holder.groupCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
             ValueEventListener  valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userType = "Student";
                        if(userType.equals(snapshot.child("userType").getValue().toString())){
                            Intent intent = new Intent(context, StudyMaterialStudentEndActivity.class);
                            intent.putExtra("groupName", groupData.getGroupName());
                            context.startActivity(intent);
                        }else{
                            Intent intent = new Intent(context, StudyMaterialTeacherEndActivity.class);
                            intent.putExtra("groupName", groupData.getGroupName());
                            context.startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve user type and group list" + error, Toast.LENGTH_SHORT).show();
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
        return groupDataArrayList.size();
    }
}

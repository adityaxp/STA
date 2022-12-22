package com.aditya.projectapp.sta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.GroupChatViewHolder>{

    Context context;
    ArrayList<GroupChatData> groupChatDataArrayList;
    String sender;
    Boolean pictureMode = false;
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int PIC_TYPE_LEFT = 2;
    public static  final int MSG_TYPE_RIGHT = 1;
    public static  final int PIC_TYPE_RIGHT = 3;



    public GroupChatAdapter(Context context, ArrayList<GroupChatData> groupChatDataArrayList, String sender) {
        this.context = context;
        this.groupChatDataArrayList = groupChatDataArrayList;
        this.sender = sender;
    }


    public GroupChatAdapter(Context context, ArrayList<GroupChatData> groupChatDataArrayList, String sender, Boolean pictureMode) {
        this.context = context;
        this.groupChatDataArrayList = groupChatDataArrayList;
        this.sender = sender;
        this.pictureMode = pictureMode;
    }

    @NonNull
    @Override
    public GroupChatAdapter.GroupChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyler_item_right, parent, false);
            GroupChatAdapter.GroupChatViewHolder groupChatViewHolder = new GroupChatAdapter.GroupChatViewHolder(view);
            return groupChatViewHolder;
        }else if(viewType == PIC_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_recyler_item_right, parent, false);
            GroupChatAdapter.GroupChatViewHolder groupChatViewHolder = new GroupChatAdapter.GroupChatViewHolder(view);
            return groupChatViewHolder;
        }else if(viewType == PIC_TYPE_LEFT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_recyler_item_left, parent, false);
            GroupChatAdapter.GroupChatViewHolder groupChatViewHolder = new GroupChatAdapter.GroupChatViewHolder(view);
            return groupChatViewHolder;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyler_item_left, parent, false);
            GroupChatAdapter.GroupChatViewHolder groupChatViewHolder = new GroupChatAdapter.GroupChatViewHolder(view);
            return groupChatViewHolder; }

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyler_item_left, parent, false);
//        GroupChatAdapter.GroupChatViewHolder groupChatViewHolder = new GroupChatAdapter.GroupChatViewHolder(view);
//        return groupChatViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapter.GroupChatViewHolder holder, int position) {
        GroupChatData groupChatData = groupChatDataArrayList.get(position);

        Glide.with(context)
                .load(groupChatDataArrayList.get(position).getUserImage())
                .into(holder.profileImage);


        holder.showMessage.setText(groupChatData.getMessage());


        if(pictureMode){
            Glide.with(context)
                    .load(groupChatDataArrayList.get(position).getSentPhoto())
                    .into(holder.SentImage);
        //    holder.SentImage.setImageResource(R.drawable.noimage);


        }

        holder.senderName.setText(groupChatData.getSender());
        holder.time.setText(groupChatData.getMessageTime());


    }


    public static class GroupChatViewHolder extends RecyclerView.ViewHolder{

        public TextView showMessage, senderName, time;
        public ImageView profileImage;
        public  ImageView SentImage;


        public GroupChatViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            senderName = itemView.findViewById(R.id.senderName);
            time = itemView.findViewById(R.id.time);
            SentImage = itemView.findViewById(R.id.SentImage);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!pictureMode &&  groupChatDataArrayList.get(position).getSender().equals(sender)) {
            return MSG_TYPE_RIGHT;
        }
        else if(pictureMode && groupChatDataArrayList.get(position).getSender().equals(sender) ){
            return PIC_TYPE_RIGHT;
        }else if(pictureMode && !groupChatDataArrayList.get(position).getSender().equals(sender)){
            return PIC_TYPE_LEFT;
        }
        else if(!pictureMode && !groupChatDataArrayList.get(position).getSender().equals(sender))  {
            return MSG_TYPE_LEFT;
        }else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return groupChatDataArrayList.size();
    }
}

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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    Context context;
    ArrayList<ChatData> chatDataArrayList;
    String sender;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, ArrayList<ChatData> chatDataArrayList, String sender) {
        this.context = context;
        this.chatDataArrayList = chatDataArrayList;
        this.sender = sender;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_recyler_item_right, parent, false);
            ChatAdapter.ChatViewHolder chatViewHolder = new ChatAdapter.ChatViewHolder(view);
            return chatViewHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_recyler_item_left, parent, false);
            ChatAdapter.ChatViewHolder chatViewHolder = new ChatAdapter.ChatViewHolder(view);
            return chatViewHolder;
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        public TextView showMessage, senderName, time;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            time = itemView.findViewById(R.id.time);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {

        ChatData chatData = chatDataArrayList.get(position);

        holder.showMessage.setText(chatData.getMessage());
        holder.time.setText(chatData.getMessageTime());

    }

    @Override
    public int getItemCount() {
        return chatDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatDataArrayList.get(position).getSenderName().equals(sender)){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

}

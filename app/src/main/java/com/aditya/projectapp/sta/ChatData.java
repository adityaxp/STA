package com.aditya.projectapp.sta;

public class ChatData {

    String chatToken, senderName, message, messageTime, senderId, receiverId, sentPhoto;


    public ChatData(String chatToken, String senderName, String message, String messageTime, String senderId, String receiverId, String sentPhoto) {
        this.chatToken = chatToken;
        this.senderName = senderName;
        this.messageTime = messageTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sentPhoto = sentPhoto;
    }

    public ChatData(String chatToken, String senderName, String message, String messageTime, String senderId, String receiverId) {
        this.chatToken = chatToken;
        this.senderName = senderName;
        this.message = message;
        this.messageTime = messageTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }


    public ChatData() {
    }

    public String getChatToken() {
        return chatToken;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}

package com.aditya.projectapp.sta;

public class GroupChatData {

    String  groupName, sender, message, userImage, messageTime, sentPhoto, messageType;


    public GroupChatData(String groupName, String sender, String message, String userImage, String messageTime, String messageType) {
        this.groupName = groupName;
        this.sender = sender;
        this.message = message;
        this.userImage = userImage;
        this.messageTime = messageTime;
        this.messageType = messageType;
    }

    public GroupChatData(String groupName, String sender, String message, String userImage, String messageTime, String sentPhoto, String messageType) {
        this.groupName = groupName;
        this.sender = sender;
        this.message = message;
        this.userImage = userImage;
        this.messageTime = messageTime;
        this.sentPhoto = sentPhoto;
        this.messageType = messageType;
    }

    public GroupChatData() {
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getSentPhoto() {
        return sentPhoto;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getMessageTime() {
        return messageTime;
    }

}

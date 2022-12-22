package com.aditya.projectapp.sta;

import java.util.ArrayList;

public class GroupData {


    String groupName, groupKey, recentMessage, groupImage, createdBy, adminEmail;


    public GroupData(String groupName, String groupKey, String recentMessage, String groupImage, String createdBy, String adminEmail) {
        this.groupName = groupName;
        this.groupKey = groupKey;
        this.recentMessage = recentMessage;
        this.groupImage = groupImage;
        this.createdBy = createdBy;
        this.adminEmail = adminEmail;
    }

    public GroupData() {
    }


    public String getGroupImage() {
        return groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getAdminEmail() {
        return adminEmail;
    }


}

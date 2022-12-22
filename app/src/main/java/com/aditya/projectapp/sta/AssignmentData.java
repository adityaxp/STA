package com.aditya.projectapp.sta;


public class AssignmentData {

    String assignmentTitle, assignmentDescription ,fileUrl, createdOn, groupName, fileType, timeStamp;


    public AssignmentData(String assignmentTitle, String assignmentDescription, String fileUrl, String createdOn, String groupName, String fileType, String timeStamp) {
        this.assignmentTitle = assignmentTitle;
        this.assignmentDescription = assignmentDescription;
        this.fileUrl = fileUrl;
        this.createdOn = createdOn;
        this.groupName = groupName;
        this.fileType = fileType;
        this.timeStamp = timeStamp;
    }

    public AssignmentData(){

    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}

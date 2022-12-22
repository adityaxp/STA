package com.aditya.projectapp.sta;

public class StudentAssignmentData {

    String  studentName ,assignmentTitle, assignmentDescription, fileType, fileUrl ,createdOn, groupName, timeStamp, approvalStatus, studentUID;

    public StudentAssignmentData() {
    }

    public StudentAssignmentData(String studentName, String assignmentTitle, String assignmentDescription, String fileType, String fileUrl, String createdOn, String groupName, String timeStamp, String approvalStatus, String studentUID) {
        this.studentName = studentName;
        this.assignmentTitle = assignmentTitle;
        this.assignmentDescription = assignmentDescription;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.createdOn = createdOn;
        this.groupName = groupName;
        this.timeStamp = timeStamp;
        this.approvalStatus = approvalStatus;
        this.studentUID = studentUID;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public String getFileType() {
        return fileType;
    }

    public String getApprovalStatus() {
        return approvalStatus;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getStudentUID() {
        return studentUID;
    }
}

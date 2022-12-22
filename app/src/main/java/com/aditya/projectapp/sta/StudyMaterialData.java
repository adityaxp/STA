package com.aditya.projectapp.sta;

public class StudyMaterialData {

    String studyMaterialTitle, studyMaterialDescription ,fileUrl, createdOn, groupName, fileType, timeStamp;


    public StudyMaterialData(String studyMaterialTitle, String studyMaterialDescription, String fileUrl, String createdOn, String groupName, String fileType, String timeStamp) {
        this.studyMaterialTitle = studyMaterialTitle;
        this.studyMaterialDescription = studyMaterialDescription;
        this.fileUrl = fileUrl;
        this.createdOn = createdOn;
        this.groupName = groupName;
        this.fileType = fileType;
        this.timeStamp = timeStamp;
    }

    public StudyMaterialData(){

    }


    public String getStudyMaterialTitle() {
        return studyMaterialTitle;
    }

    public String getStudyMaterialDescription() {
        return studyMaterialDescription;
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

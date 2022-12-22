package com.aditya.projectapp.sta;

public class TeacherData {

    String userName, userImage, userEmail, userInstitute,userType;

    public TeacherData(String userName, String userImage) {
        this.userName = userName;
        this.userImage = userImage;
    }

    public TeacherData() {
    }

    public TeacherData(String userName, String userImage, String userEmail, String userInstitute, String userType) {
        this.userName = userName;
        this.userImage = userImage;
        this.userEmail = userEmail;
        this.userInstitute = userInstitute;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public String getUserInstitute() {
        return userInstitute;
    }

    public String getUserType() {
        return userType;
    }
}

package com.atharvakale.facerecognition.data.model;

public class ViewAttandnessModel {
    private  String rollno;
    private  String getUid;
    private  String stName;
    private  String stClass;
    private  String stCourse;
    private  String sCreen;

    public ViewAttandnessModel(String rollno, String stName, String stClass,String getUid,String stCourse,String sCreen) {
        this.rollno = rollno;
        this.stName = stName;
        this.stClass = stClass;
        this.getUid=getUid;
        this.stCourse=stCourse;
        this.sCreen=sCreen;
    }

    public String getsCreen() {
        return sCreen;
    }

    public void setsCreen(String sCreen) {
        this.sCreen = sCreen;
    }

    public String getRollNo() {
        return rollno;
    }
    public void setGetUid(String getUid){this.getUid=getUid;}
    public  String  getGetUid(){
        return getUid;
    }
    public void setRollNo(String rollno) {
        this.rollno = rollno;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getStClass() {
        return stClass;
    }

    public void setStClass(String stClass) {
        this.stClass = stClass;
    }

    public String getStCourse() {
        return stCourse;
    }

    public void setStCourse(String stCourse) {
        this.stCourse = stCourse;
    }
}

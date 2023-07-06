package com.atharvakale.facerecognition.moduleclasses;

import java.util.Map;

public class UserModel {
    String name, rollno, className;
   // Bitmap bitmapImage;
    Map<String, Object> mapData;

    public Map<String, Object> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, Object> mapData) {
        this.mapData = mapData;
    }

    public UserModel() {
    }

    public UserModel(String name, String rollno, String className, Map<String, Object> mapData) {
        this.name = name;
        this.rollno = rollno;
        this.className = className;
        this.mapData = mapData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollno;
    }

    public void setRollNo(String rollno) {
        this.rollno = rollno;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}

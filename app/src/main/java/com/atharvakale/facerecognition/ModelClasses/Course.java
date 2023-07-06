package com.atharvakale.facerecognition.ModelClasses;
public class Course {
    private String name;
    private String CourseDocId;
    private String id;
    private String clasName;
    private String active;

    public Course(String name, String id, String clasName, String active,String CourseDocId) {
        this.name = name;
        this.id = id;
        this.clasName = clasName;
        this.active = active;
        this.CourseDocId=CourseDocId;
    }

    public String getCourseDocId() {
        return CourseDocId;
    }

    public void setCourseDocId(String courseDocId) {
        CourseDocId = courseDocId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClasName() {
        return clasName;
    }

    public void setClasName(String clasName) {
        this.clasName = clasName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    // Constructors, getters, and setters
}

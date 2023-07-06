package com.atharvakale.facerecognition.ModelClasses;

public class ClassModel {
    public String className;
    public String active;
    public String id;

    public ClassModel(String className, String active,String id) {
        this.className = className;
        this.active = active;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}

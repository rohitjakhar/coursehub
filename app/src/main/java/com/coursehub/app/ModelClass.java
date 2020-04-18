package com.coursehub.app;

public class ModelClass {
    private String courseTitle;
    private String courseBody;
    private String courseLink;

    public ModelClass() {

    }

    public ModelClass(String courseTitle, String courseBody, String courseLink) {
        this.courseTitle = courseTitle;
        this.courseBody = courseBody;
        this.courseLink = courseLink;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseBody() {
        return courseBody;
    }

    public String getCourseLink() {
        return courseLink;
    }

}

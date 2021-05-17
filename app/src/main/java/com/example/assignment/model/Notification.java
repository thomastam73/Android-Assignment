package com.example.assignment.model;

public class Notification {
    private String userid;
    private String text;
    private String postid;

    public Notification(String userid, String text, String postid) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
    }

    public Notification() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}

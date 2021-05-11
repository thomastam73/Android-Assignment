package com.example.assignment.model;

import android.net.Uri;

public class Post {
    private String title;
    private String address;
    private String description;
    private String author;
    private String postDate;
    private String cover;




    public Post() {
    }


    public Post(String title, String address, String description, String author, String postDate, String cover) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.author = author;
        this.postDate = postDate;
        this.cover = cover;
    }

    public Post(String title, String address, String description, String author, String postDate) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.author = author;
        this.postDate = postDate;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

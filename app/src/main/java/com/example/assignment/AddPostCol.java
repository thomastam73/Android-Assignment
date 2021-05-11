package com.example.assignment;

import java.util.Date;

public class AddPostCol {
    private String title;
    private String address;
    private String description;
    private String author;
    private String postDate;




    public AddPostCol() {
    }
    public AddPostCol(String title, String address, String description, String author, String postDate) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.author = author;
        this.postDate = postDate;
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

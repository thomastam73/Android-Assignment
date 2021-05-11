package com.example.assignment;

import android.net.Uri;

public class AddUserCol {
    private String id;
    private String name;
    private String email;
    private String description;
    private Uri icon;
    private int age;
    private int follower;
    private int following;

    public AddUserCol(String id, String name, String email, String description, Uri icon, int age, int follower, int following) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = description;
        this.icon = icon;
        this.age = age;
        this.follower = follower;
        this.following = following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}

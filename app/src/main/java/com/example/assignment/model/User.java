package com.example.assignment.model;

import android.net.Uri;

public class User {
    private String id;
    private String icon;
    private String name;
    private String email;
    private String description;
    private int age;
    private int follower;
    private int following;

    public User() {
    }

    public User(String id, String icon, String name, String email, String description, int age, int follower, int following) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.email = email;
        this.description = description;
        this.age = age;
        this.follower = follower;
        this.following = following;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

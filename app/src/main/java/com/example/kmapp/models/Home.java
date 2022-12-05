package com.example.kmapp.models;

public class Home {

    private String name,timestamp, profileImage, imageUrl, uid;
    private int accountlike;

    public Home(String name, String timestamp, String profileImage, String imageUrl, String uid, int accountlike) {
        this.name = name;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.accountlike = accountlike;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAccountlike() {
        return accountlike;
    }

    public void setAccountlike(int accountlike) {
        this.accountlike = accountlike;
    }
}

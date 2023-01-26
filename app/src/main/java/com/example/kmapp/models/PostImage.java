package com.example.kmapp.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PostImage {

    private String imageUrl, id, description, uid;

    //lấy Dấu thời gian của máy chủ từ Firestore trong thiết bị Android?
    @ServerTimestamp
    private Date timestamp;

    public PostImage(){

    }

    public PostImage(String imageUrl, String id, String description, String uid, Date timestamp) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.description = description;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

package com.example.SocialMedia1.Model;

public class Posts {
    private String date;
    private String postid;
    private  String postImage;
    private  String description;
    private  String publisher;
    private String profile;
    private  String memer;
    private  String username;
    private long counterPost;


    public Posts(String date, String postid, String postImage, String description, String publisher, String profile, String memer,String username,long counterPost) {
        this.date = date;
        this.postid = postid;
        this.postImage = postImage;
        this.description = description;
        this.publisher = publisher;
        this.profile = profile;
        this.memer = memer;
        this.username = username;
        this.counterPost = counterPost;
    }

    public Posts()
    {

    }

    public String getDate() {
        return date;
    }

    public String getPostid() {
        return postid;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getProfile() {
        return profile;
    }

    public String getMemer() {
        return memer;
    }

    public String getUsername() {
        return username;
    }

    public long getCounterPost() {
        return counterPost;
    }
}

package com.example.SocialMedia1.Model;

public class Data {
    String username;
    String email;
    String memer;
    String user_id;
    String background;
    String profileUrl;


    public Data(String username, String email, String memer, String user_id, String background, String profileUrl) {
        this.username = username;
        this.email = email;
        this.memer = memer;
        this.user_id = user_id;
        this.background = background;
        this.profileUrl = profileUrl;
    }

    public Data(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemer() {
        return memer;
    }

    public void setMemer(String memer) {
        this.memer = memer;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}

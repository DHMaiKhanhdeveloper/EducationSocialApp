package com.example.SocialMedia1.Model;

public class Comment {
    String comment;
    String publisher;
    String time;

    public Comment(String comment, String publisher, String time) {
        this.comment = comment;
        this.publisher = publisher;
        this.time = time;
    }

    public Comment()
    {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

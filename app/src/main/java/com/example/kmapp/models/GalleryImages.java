package com.example.kmapp.models;

import android.net.Uri;

public class GalleryImages {

    public Uri picUri;

    public GalleryImages(Uri picUri) {
        this.picUri = picUri;
    }

    public GalleryImages() {

    }

    public Uri getPicUri() {
        return picUri;
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri;
    }
}

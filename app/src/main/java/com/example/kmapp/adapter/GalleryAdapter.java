package com.example.kmapp.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kmapp.R;
import com.example.kmapp.models.GalleryImages;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {

     List<GalleryImages> list;

     SendImage onSendImage;



    public void SendImage(SendImage onSendImage) {
        this.onSendImage = onSendImage;
    }

    public GalleryAdapter(List<GalleryImages> list) {
        this.list = list;
    }

    private void chooseImage(Uri picUri) {

        onSendImage.onSend(picUri);

    }


    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_items, parent, false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getPicUri())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> chooseImage(list.get(holder.getAbsoluteAdapterPosition()).getPicUri()));
    }


    public interface SendImage {
        void onSend(Uri picUri);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GalleryHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

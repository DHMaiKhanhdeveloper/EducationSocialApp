package com.example.kmapp.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kmapp.R;
import com.example.kmapp.models.Home;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder>{

    private List<Home> list;
    private Activity context;

    private FirebaseUser user;
//    private OnPressed onPressed;


    public HomeAdapter(List<Home> list, Activity context) {
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        Home home = list.get(position);
        if(home == null){
            return;
        }
        holder.tvUserName.setText(home.getName());
        holder.tvTime.setText( home.getTimestamp());

        Random random = new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        int count = home.getAccountlike();

        if (count == 0) {
            holder.tvLikeCount.setText("0 Like");
        } else if (count == 1) {
            holder.tvLikeCount.setText(count + " Like");
        } else {
            holder.tvLikeCount.setText(count + " Likes");
        }

        Glide.with(context.getApplicationContext())
                .load(home.getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.cimgProfileImage);

        Glide.with(context.getApplicationContext())
                .load(home.getImageUrl())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    class HomeHolder extends RecyclerView.ViewHolder{
        private CircleImageView cimgProfileImage;
        private  TextView tvUserName;
        private TextView tvTime;
        private  TextView tvLikeCount;
        private  ImageView imageView;
        private  CheckBox cbLikeCheckBox;
        private  ImageButton btnComment;
        private  ImageButton btnShare;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);

            cimgProfileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            tvUserName = itemView.findViewById(R.id.nameTv);
            tvTime = itemView.findViewById(R.id.timeTv);
            tvLikeCount = itemView.findViewById(R.id.likeCountTv);
            cbLikeCheckBox = itemView.findViewById(R.id.likeBtn);
            btnComment = itemView.findViewById(R.id.commentBtn);
            btnShare = itemView.findViewById(R.id.shareBtn);
//            descriptionTv = itemView.findViewById(R.id.descTv);

//            TextView commentTV = itemView.findViewById(R.id.commentTV);
        }
    }
}

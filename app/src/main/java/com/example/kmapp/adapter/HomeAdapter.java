package com.example.kmapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kmapp.R;
import com.example.kmapp.ReplaceActivity;
import com.example.kmapp.models.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder>{

    private final List<Home> list;
    Activity context;
    OnPressed onPressed;


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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        holder.tvUserName.setText(list.get(position).getName());
        holder.tvTime.setText("" + list.get(position).getTimestamp());

        List<String> likeList = list.get(position).getLikes();

        int count = likeList.size();

        if (count == 0) {
            holder.tvLikeCount.setText("0 Like");
        } else if (count == 1) {
            holder.tvLikeCount.setText(count + " Like");
        } else {
            holder.tvLikeCount.setText(count + " Likes");
        }

        //check if already like
        assert user != null;
        holder.cbLikeCheckBox.setChecked(likeList.contains(user.getUid()));

        holder.tvDescription.setText(list.get(position).getDescription());

        Random random = new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.cimgProfileImage);

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageUrl())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);

        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getName(),
                list.get(position).getUid(),
                list.get(position).getLikes(),
                list.get(position).getImageUrl()
        );

    }

    @Override
    public int getItemCount() {

            return list.size();

    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }

    public interface OnPressed {
        void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked);

        void setCommentCount(TextView textView);

    }

    public class HomeHolder extends RecyclerView.ViewHolder{
        private CircleImageView cimgProfileImage;
        private  TextView tvUserName;
        private TextView tvTime;
        private  TextView tvLikeCount;
        private  TextView tvDescription;
        private TextView tvComment;
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
            tvDescription = itemView.findViewById(R.id.descTv);

            tvComment  = itemView.findViewById(R.id.commentTV);

            onPressed.setCommentCount(tvComment);

        }


        public void clickListener(int position, String id, String name, String uid, List<String> likes, String imageUrl) {

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReplaceActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("uid", uid);
                    intent.putExtra("isComment", true);

                    context.startActivity(intent);

                }
            });

            cbLikeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onPressed.onLiked(position, id, uid, likes, isChecked);

                    btnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                            intent.setType("text/*");
                            context.startActivity(Intent.createChooser(intent, "Share link using..."));
                        }
                    });
                }
            });
        }
    }
}

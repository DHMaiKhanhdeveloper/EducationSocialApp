package com.example.SocialMedia1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SocialMedia1.Model.Comment;
import com.example.SocialMedia1.Model.Data;
import com.example.SocialMedia1.OthersProfile;
import SocialMedia1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import SocialMedia1.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        
        return new ViewHolder(view);
    
    
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
        Comment comment=commentList.get(position);

        holder.comment.setText(comment.getComment());
        holder.time.setText(comment.getTime());
        getUserInfo(holder.profile,holder.username,comment.getPublisher());


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OthersProfile.class);
                intent.putExtra("uid",comment.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OthersProfile.class);
                intent.putExtra("uid",comment.getPublisher());
                context.startActivity(intent);
            }
        });


        

    }

    @Override
    public int getItemCount() {
        return commentList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        
        TextView comment,username,time;
        CircleImageView profile;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            comment=itemView.findViewById(R.id.comment);
            username=itemView.findViewById(R.id.username);
            profile=itemView.findViewById(R.id.profile_image);
            time=itemView.findViewById(R.id.date);


        }
    }

    private void getUserInfo(final ImageView imageView,final TextView username,final String publisher )
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisher);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Data data=snapshot.getValue(Data.class);

                username.setText(data.getUsername());
                Picasso.get().load(data.getProfileUrl()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

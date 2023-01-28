package com.example.SocialMedia1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SocialMedia1.Model.Data;
import com.example.SocialMedia1.OthersProfile;
import SocialMedia1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    Context context;
    List<Data> dataList;


    public ShowAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.show_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Data data=dataList.get(position);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        holder.username.setText(data.getUsername());
        holder.memer.setText(data.getMemer());
        Picasso.get().load(data.getProfileUrl()).into(holder.profile);

        isFollowing(data.getUser_id(),holder.btn_follow,holder.btn_following);


        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_follow.getText().toString().equals("Follow")) {

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getUid())
                            .child("following").child(dataList.get(position).getUser_id()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(dataList.get(position).getUser_id())
                            .child("followers").child(user.getUid()).setValue(true);

                }
            }
        });
        holder.btn_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_following.getText().toString().equals("Following")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getUid())
                            .child("following").child(dataList.get(position).getUser_id()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(dataList.get(position).getUser_id())
                            .child("followers").child(user.getUid()).removeValue();
                }
            }
        });


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OthersProfile.class);
                intent.putExtra("uid",data.getUser_id());
                context.startActivity(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView username,memer;
        Button btn_follow,btn_following;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            memer = itemView.findViewById(R.id.memer);
            btn_follow = itemView.findViewById(R.id.btn_follow);
            btn_following = itemView.findViewById(R.id.btn_following);

        }
    }

    private void isFollowing(final String userid, final Button follow, final Button following) {

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(user.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userid).exists()) {
                    follow.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);
                } else {
                    follow.setVisibility(View.VISIBLE);
                    following.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

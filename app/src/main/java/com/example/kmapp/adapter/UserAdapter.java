package com.example.kmapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kmapp.R;
import com.example.kmapp.models.Home;
import com.example.kmapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{

     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
     List<Users> list;
    OnUserClicked onUserClicked;

    public UserAdapter(List<Users> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {

//        Users users = list.get(position);
//
//        if (list.get(position).getUid().equals(user.getUid())) {
//            holder.relativeLayout.setVisibility(View.GONE);
//            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        } else {
//            holder.relativeLayout.setVisibility(View.VISIBLE);
//        }
//
//        holder.tvName.setText(users.getName());
//        holder.tvStatus.setText(users.getStatus());
//
//        Glide.with(holder.itemView.getContext().getApplicationContext())
//                .load(users.getProfileImage())
//                .placeholder(R.drawable.ic_person)
//                .timeout(6500)
//                .into(holder.cimgProfileImage);

        if (list.get(position).getUid().equals(user.getUid())) {
            holder.relativeLayout.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(list.get(position).getName());
        holder.tvStatus.setText(list.get(position).getStatus());

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.cimgProfileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserClicked.onClicked(list.get(holder.getAdapterPosition()).getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnUserClicked(OnUserClicked onUserClicked) {
        this.onUserClicked = onUserClicked;
    }

    public interface OnUserClicked {
        void onClicked(String uid);
    }

    static public class UserHolder extends RecyclerView.ViewHolder{

        private CircleImageView cimgProfileImage;
        private TextView tvName, tvStatus;
        private RelativeLayout relativeLayout;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            cimgProfileImage = itemView.findViewById(R.id.profileImage);
            tvName = itemView.findViewById(R.id.nameTV);
            tvStatus = itemView.findViewById(R.id.statusTV);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }
}

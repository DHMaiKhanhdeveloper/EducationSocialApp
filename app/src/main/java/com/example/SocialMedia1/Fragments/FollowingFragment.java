package com.example.SocialMedia1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SocialMedia1.Adapter.PostAdapter;
import com.example.SocialMedia1.Model.Posts;
import com.example.SocialMedia1.NotificationActivity;
import com.example.SocialMedia1.OthersProfile;
import com.example.SocialMedia1.PostActivity;
import SocialMedia1.R;
import com.example.SocialMedia1.SearchUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FollowingFragment extends Fragment {


    RecyclerView recyclerView;
    List<Posts> postsList;
    PostAdapter adapter;

    CircleImageView profile;
    ImageView search;
    ImageView post_one,note,active;
    TextView no;
    Button discover;


    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference reference;

    List<String> followingList;

    



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);


        profile = view.findViewById(R.id.profile_image);
        search = view.findViewById(R.id.search);
        post_one = view.findViewById(R.id.post_one);
        no = view.findViewById(R.id.no);
        discover = view.findViewById(R.id.discover);
        note = view.findViewById(R.id.note);
        active = view.findViewById(R.id.active);



        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        postsList=new ArrayList<>();
        adapter=new PostAdapter(getActivity(),postsList);

        checkFollowing();
        recyclerView.setAdapter(adapter);
        
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchUsers.class);
                startActivity(intent);
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),NotificationActivity.class);
                startActivity(intent);
            }
        });


        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists())
               {
                   String p = snapshot.child("profileUrl").getValue().toString();

                   Picasso.get().load(p).placeholder(R.drawable.profile_image).into(profile);
               }else
               {

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        clicks();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), OthersProfile.class);
                intent.putExtra("uid",user.getUid());
                startActivity(intent);
            }
        });



        active.setVisibility(View.GONE);


        return view;
    }


    private void clicks()
    {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchUsers.class));
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchUsers.class));
            }
        });





        post_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), OthersProfile.class);
                intent.putExtra("uid",user.getUid());
                startActivity(intent);
            }
        });






    }

    private void checkFollowing()
    {
        followingList=new ArrayList<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(user.getUid()).child("following");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        followingList.add(dataSnapshot.getKey());
                    }
                    getPosts();

                    no.setVisibility(View.GONE);
                    discover.setVisibility(View.GONE);
                }else
                {
                    no.setVisibility(View.VISIBLE);
                    discover.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void getPosts()
    {
        DatabaseReference postRef=FirebaseDatabase.getInstance().getReference().child("Posts");
        Query query=postRef.orderByChild("counterPost");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Posts posts=dataSnapshot.getValue(Posts.class);

                    for (String id : followingList)
                    {
                        if (posts.getPublisher().equals(id))
                        {

                            postsList.add(posts);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}
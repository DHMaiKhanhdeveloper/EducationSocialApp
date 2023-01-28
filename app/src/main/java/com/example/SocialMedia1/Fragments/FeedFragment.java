package com.example.SocialMedia1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.SocialMedia1.Adapter.PostAdapter;
import com.example.SocialMedia1.Model.Posts;
import SocialMedia1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    List<Posts> postsList;
    PostAdapter adapter;


    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference reference,postRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed, container, false);



        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postsList=new ArrayList<>();
        adapter=new PostAdapter(getActivity(),postsList);
        recyclerView.setAdapter(adapter);


        getPosts();




        return view;
    }

    private void getPosts()
    {
        Query query= postRef.orderByChild("counterPost");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Posts posts=dataSnapshot.getValue(Posts.class);
                    if (posts.getCounterPost()>=0)
                    {
                        postsList.add(posts);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}




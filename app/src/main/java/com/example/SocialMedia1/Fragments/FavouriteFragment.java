package com.example.SocialMedia1.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SocialMedia1.Adapter.PhotosAdapter;
import com.example.SocialMedia1.Model.Posts;
import SocialMedia1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment {

    private List<String> mySaves;
    RecyclerView recyclerView_save;
    List<Posts> mPost_save;
    PhotosAdapter adapter;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourite2, container, false);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference().child("Users");


        recyclerView_save=view.findViewById ( R.id.recyclerView );
        recyclerView_save.setHasFixedSize ( true );
        recyclerView_save.setLayoutManager(new GridLayoutManager(getContext(),3));

        mPost_save=new ArrayList<>(  );
        adapter=new PhotosAdapter ( getContext (),mPost_save );
        recyclerView_save.setAdapter ( adapter );

        Saved();
        return view;
    }

    private void Saved(){
        mySaves=new ArrayList<> (  );
        DatabaseReference reference=FirebaseDatabase.getInstance ().getReference ().child ( "Favourites" )
                .child ( user.getUid () );

        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren ()){
                    mySaves.add ( snapshot.getKey () );
                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void readSaves() {
        DatabaseReference reference= FirebaseDatabase.getInstance ().getReference ().child ( "Posts" );

        reference.addValueEventListener ( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost_save.clear ();
                for (DataSnapshot snapshot : dataSnapshot.getChildren ()){
                    Posts post=snapshot.getValue (Posts.class);

                    for (String id : mySaves){
                        if (post.getPostid ().equals ( id )){
                            mPost_save.add ( post );
                        }
                    }
                }
                adapter.notifyDataSetChanged ();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }



}
package com.example.SocialMedia1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SocialMedia1.Adapter.ShowAdapter;
import com.example.SocialMedia1.Model.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import SocialMedia1.R;

public class ShowList extends AppCompatActivity {

    String id;
    String title;
    TextView title_tv;
    List<String> list;

    Toolbar toolbar;

    RecyclerView recyclerView;
    ShowAdapter adapter;
    List<Data> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        title_tv=findViewById(R.id.title);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        title=intent.getStringExtra("title");

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_tv.setText(title);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        data=new ArrayList<>();
        adapter=new ShowAdapter(this,data);
        recyclerView.setAdapter(adapter);

        list=new ArrayList<>();

        switch (title)
        {
            case "Followers":
                getFollowers();
                break;
            case "Following":
                getFollowing();
                break;
        }



    }

    private void getFollowers() {


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(id).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    list.add(dataSnapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ShowList.this, "Error loading..", Toast.LENGTH_SHORT).show();
            }
        });








    }
    private void getFollowing() {


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(id).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    list.add(dataSnapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ShowList.this, "Error loading..", Toast.LENGTH_SHORT).show();
            }
        });








    }

    private void showUsers()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Data user=dataSnapshot.getValue(Data.class);
                    for (String id : list)
                    {
                        if (user.getUser_id().equals(id))
                        {
                            data.add(user);
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
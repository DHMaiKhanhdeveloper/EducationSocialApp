package com.example.SocialMedia1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SocialMedia1.Adapter.CommentAdapter;
import com.example.SocialMedia1.Model.Comment;
import com.example.SocialMedia1.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import SocialMedia1.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    CircleImageView profile;
    EditText edit_comment;
    ImageView send;
    Toolbar toolbar;
    TextView no;

    RecyclerView recyclerView;
    List<Comment> commentList;
    CommentAdapter adapter;


    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    String postid;
    String publisher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        profile=findViewById(R.id.profile_image);
        edit_comment=findViewById(R.id.comment_edit);
        send=findViewById(R.id.send);
        no=findViewById(R.id.no);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference().child("Users");


        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
        publisher=intent.getStringExtra("publisher");

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        commentList=new ArrayList<>();
        adapter=new CommentAdapter(this,commentList);

        getComments();

        recyclerView.setAdapter(adapter);

        getImage();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_comment.getText().toString().isEmpty())
                {
                    Toast.makeText(CommentActivity.this, "Comment can't be empty", Toast.LENGTH_SHORT).show();
                }else
                {
                    addComment();
                }
            }
        });



    }

    private void getComments() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(postid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    commentList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Comment comment=dataSnapshot.getValue(Comment.class);
                        commentList.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                    no.setVisibility(View.GONE);
                }else
                {
                    no.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CommentActivity.this, "Error while load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addComment() {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("dd-M-yyyy hh:mm a");
        String currentDate=format.format(date);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        HashMap<String,Object> map=new HashMap<>();
        map.put("comment",edit_comment.getText().toString());
        map.put("publisher",user.getUid());
        map.put("time",currentDate);

        databaseReference.push().setValue(map);
        addNotifications();
        edit_comment.setText("");
    }
    private void addNotifications()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisher);

        HashMap<String,Object> map=new HashMap<>();

        map.put("userid",user.getUid());
        map.put("comment","Commented: "+edit_comment.getText().toString());
        map.put("postid",postid);
        map.put("ispost",true);

        reference.push().setValue(map);
    }
    private void getImage()
    {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Data data=snapshot.getValue(Data.class);

                Picasso.get().load(data.getProfileUrl()).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

//
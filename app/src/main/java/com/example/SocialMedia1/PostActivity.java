package com.example.SocialMedia1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import SocialMedia1.R;

public class PostActivity extends AppCompatActivity {
    ImageView cancel,pick_image;
    TextView post;
    EditText description;
    CardView post_card;
    ProgressDialog progressDialog;

//    profileUrl,username,memer
    String post_url;
    Uri postUri;

    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference userRef,postRef;
    StorageReference storageReference;

    private long counterPost=0;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        init();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference().child("Posts");
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        postRef=FirebaseDatabase.getInstance().getReference().child("Posts");


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PostActivity.this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

//        getData();

        pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this,HomeActivity.class));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK)
        {
            if (data !=null)
            {
                postUri=data.getData();
                pick_image.setImageURI(postUri);
                pick_image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }

        }else
        {
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPost()
    {
        progressDialog=new ProgressDialog(PostActivity.this);
        progressDialog.setTitle("New Post");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (postUri == null)
        {
            Toast.makeText(this, "No Meme select!!", Toast.LENGTH_SHORT).show();
        }else
        {
            final StorageReference sRef=storageReference.child(System.currentTimeMillis()+"."+fileExtension(postUri));
            sRef.putFile(postUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            post_url=uri.toString();
                            //database

//                            SharedPreferences.Editor editor=getSharedPreferences("post",MODE_PRIVATE).edit();
//                            editor.putString("id",postid);
//                            editor.apply();

                            savePostsDataInFirebase(post_url);



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                  double progress=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                  progressDialog.setMessage("Uploaded "+((int)progress) +"%...");


                }
            });
        }
    }

    private void init()
    {
        cancel=findViewById(R.id.cancel_post);
        pick_image=findViewById(R.id.pick);
        post=findViewById(R.id.post_upload);
        pick_image=findViewById(R.id.pick);
        post_card=findViewById(R.id.post_card);
        description=findViewById(R.id.post_description);

    }

    public String fileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap map=MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

//    private void getData(){
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Data data=snapshot.getValue(Data.class);
//
//                profileUrl=data.getProfileUrl();
//                username=data.getUsername();
//                memer=data.getMemer();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void savePostsDataInFirebase(final String url)
    {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    counterPost=snapshot.getChildrenCount();
                }else
                {
                    counterPost=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String n=snapshot.child("username").getValue().toString();
                    String m=snapshot.child("memer").getValue().toString();
                    String p=snapshot.child("profileUrl").getValue().toString();

                    Date date=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("dd-M-yyyy hh:mm a");
                    String currentDate=format.format(date);

                    String postid=postRef.push().getKey();
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("date",currentDate);
                    map.put("postid",postid);
                    map.put("postImage",url);
                    map.put("description",description.getText().toString());
                    map.put("publisher",user.getUid());
                    map.put("profile",p);
                    map.put("memer",m);
                    map.put("username",n);
                    map.put("counterPost",counterPost);

                    postRef.child(postid).updateChildren(map);

                    progressDialog.dismiss();

                    Toast.makeText(PostActivity.this, "New post added!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostActivity.this,HomeActivity.class));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    
}
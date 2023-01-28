package com.example.SocialMedia1.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SocialMedia1.Adapter.PhotosAdapter;
import com.example.SocialMedia1.Login;
import com.example.SocialMedia1.Model.Posts;
import SocialMedia1.R;
import com.example.SocialMedia1.ShowList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment {


    TextView followers,following,logout;
    Button btn_update,u_bg,settings;
    ImageView profile;
    ImageView bg,bg_camera;
    TextView username,memer,following_count,followers_count,pos_count;
    ProgressDialog pd;

    RecyclerView recyclerView;
    List<Posts> postsList;
    PhotosAdapter adapter;

    FirebaseAuth auth;
    FirebaseUser user;
    String profileid;
    String id;

    DatabaseReference reference;
    StorageReference storageReference,bgRef;
    Uri profileUri,bgUri;



    public UserFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);



        SharedPreferences preferences=getContext().getSharedPreferences("USERS", Context.MODE_PRIVATE);
        profileid=preferences.getString("profileid","none");

        init(view);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference= FirebaseStorage.getInstance().getReference().child("Profiles");
        bgRef= FirebaseStorage.getInstance().getReference().child("Backgrounds");

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        postsList=new ArrayList<>();
        adapter=new PhotosAdapter(getContext(),postsList);


        recyclerView.setAdapter(adapter);
        getImages();


        clicks();
        getUserData();
        getFollowCount();

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShowList.class);
                intent.putExtra("id",profileid);
                intent.putExtra("title",followers.getText().toString());
                startActivity(intent);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShowList.class);
                intent.putExtra("id",profileid);
                intent.putExtra("title",following.getText().toString());
                startActivity(intent);
            }
        });


        return view;
//

    }

    private void clicks()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), Login.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);

            }
        });
        bg_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (profileUri==null)
                {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,2);
                }else
                {
                    uploadProfile();
                }


            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfile();
            }
        });

        u_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBackground();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            profileUri=data.getData();
            profile.setImageURI(profileUri);
            btn_update.setVisibility(View.VISIBLE);

        }else if (requestCode==2 && resultCode==RESULT_OK && data !=null)
        {
            bgUri=data.getData();
            bg.setImageURI(bgUri);
            bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            u_bg.setVisibility(View.VISIBLE);
        }
    }

    private void uploadProfile()
    {
        if (profileUri !=null)
        {
            pd.setTitle("Profile Picture");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            final StorageReference sRef=storageReference.child(user.getUid()+"."+getFileExtension(profileUri));
            sRef.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String profileUrl=uri.toString();

                            HashMap<String,Object> map=new HashMap<>();
                            map.put("profileUrl",profileUrl);


                            reference.child(user.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Profiled updated", Toast.LENGTH_SHORT).show();
                                        btn_update.setVisibility(View.GONE);
                                    }else
                                    {
                                        pd.dismiss();
                                        btn_update.setVisibility(View.VISIBLE);
                                        Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    pd.setMessage("Updating... "+((int)progress) +"%...");

                }
            });


        }else
        {
            Toast.makeText(getContext(), "No image is selected!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateBackground()
    {
        if (bgUri !=null)
        {
            pd.setTitle("Background image");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            final StorageReference sRef=bgRef.child(user.getUid()+"."+getFileExtension(bgUri));
            sRef.putFile(bgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String bgUrl=uri.toString();

                            HashMap<String,Object> map=new HashMap<>();
                            map.put("background",bgUrl);

                            reference.child(user.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Background image updated..", Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    pd.setMessage("Updating... "+((int)progress) +"%...");

                }
            });


        }else
        {
            Toast.makeText(getContext(), "No image is selected!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFollowCount()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(user.getUid()).child("followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers_count.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(user.getUid()).child("following");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following_count.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference postCount=FirebaseDatabase.getInstance().getReference().child("Posts");
        postCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String p=dataSnapshot.child("publisher").getValue().toString();
                    if (p.equals(user.getUid()))
                    {
                        i++;
                    }
                }
                pos_count.setText("Posts "+"("+i+")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getUserData()
    {

        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String n=snapshot.child("username").getValue().toString();
                String m=snapshot.child("memer").getValue().toString();
                String p=snapshot.child("profileUrl").getValue().toString();
                String b=snapshot.child("background").getValue().toString();


                username.setText(n);
                memer.setText(m);
                Picasso.get().load(p).placeholder(R.drawable.profile_image).into(profile);
                Picasso.get().load(b).into(bg);


//                Glide.with(getActivity()).load(p).centerCrop().placeholder(R.drawable.profile_image).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });

    }



    public String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap map=MimeTypeMap.getSingleton();
        return map.getMimeTypeFromExtension(contentResolver.getType(uri));
    }
    private void init(View view)
    {
        settings=view.findViewById(R.id.settings);

        followers=view.findViewById(R.id.followers);
        following=view.findViewById(R.id.following);
        logout=view.findViewById(R.id.logout);
        profile=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);
        memer=view.findViewById(R.id.memer);
        btn_update=view.findViewById(R.id.btn_update);
        u_bg=view.findViewById(R.id.update_bg);
        bg=view.findViewById(R.id.background);
        following_count=view.findViewById(R.id.following_count);
        followers_count=view.findViewById(R.id.followers_count);
        pos_count=view.findViewById(R.id.posts);
        bg_camera=view.findViewById(R.id.upload_background);
        pd=new ProgressDialog(getContext());


    }

    private void getImages()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Posts posts=dataSnapshot.getValue(Posts.class);
                    if (posts.getPublisher().equals(user.getUid()))
                    {
                        postsList.add(posts);
                    }
                }
                Collections.reverse(postsList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }









}



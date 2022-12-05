package com.example.kmapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kmapp.MainActivity;
import com.example.kmapp.R;
import com.example.kmapp.ReplaceActivity;
import com.example.kmapp.models.PostImage;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private Toolbar toolbar;
    private TextView tvName, tvToolbarName, tvStatus, tvFollowingCount, tvFollowersCount, tvPostCount;
    private CircleImageView cimgProfileImage;
    private Button btnFollow, btnStartChat;
    private RecyclerView rcvProfile;
    private LinearLayout linearCountLayout;
    private ImageButton btnEditProfile,btnSend;
    private FirestoreRecyclerAdapter<PostImage, PostImageHolder> adapter;
    private FirebaseUser firebaseUser;
    private DocumentReference userRef,myRef;
    private FirebaseAuth auth;
    private boolean isMyProfile = true;
    private int count;
    private    String userUID;
//    private FirestoreRecyclerAdapter<PostImage, PostImageHolder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
//        myRef = FirebaseFirestore.getInstance().collection("Users")
//                .document(firebaseUser.getUid());
        userRef = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());

        loadDataBasic();
        if (isMyProfile) {
            btnEditProfile.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.GONE);
            linearCountLayout.setVisibility(View.VISIBLE);

            //Hide chat btn
            btnStartChat.setVisibility(View.GONE);

        } else {
            btnEditProfile.setVisibility(View.GONE);
            btnFollow.setVisibility(View.VISIBLE);
//            countLayout.setVisibility(View.GONE);
        }
        rcvProfile.setHasFixedSize(true);
        rcvProfile.setLayoutManager(new GridLayoutManager(getContext(), 3));

        loadPostImages();

//        rcvProfile.setAdapter(adapter);
    }
    private void loadPostImages(){
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(userUID);

        Query query = reference.collection("Post Images");

        FirestoreRecyclerOptions<PostImage> options = new FirestoreRecyclerOptions.Builder<PostImage>()
                .setQuery(query, PostImage.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostImage, PostImageHolder>(options){

            @NonNull
            @Override
            public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_items, parent, false);
                return new PostImageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImage model) {
                Glide.with(holder.itemView.getContext().getApplicationContext())
                        .load(model.getImageUrl())
                        .timeout(6500)
                        .into(holder.imageView);
                count = getItemCount();
                tvPostCount.setText("" + count);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void init(View view) {

        toolbar = view.findViewById(R.id.toolbar);
//        assert getActivity() != null;
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        tvName = view.findViewById(R.id.nameTv);
        tvStatus = view.findViewById(R.id.statusTV);
        tvToolbarName = view.findViewById(R.id.toolbarNameTV);
        tvFollowersCount = view.findViewById(R.id.followersCountTv);
        tvFollowingCount = view.findViewById(R.id.followingCountTv);
        tvPostCount = view.findViewById(R.id.postCountTv);
        cimgProfileImage = view.findViewById(R.id.profileImage);
        btnFollow = view.findViewById(R.id.followBtn);
        rcvProfile = view.findViewById(R.id.recyclerView);
        linearCountLayout = view.findViewById(R.id.countLayout);
        btnEditProfile = view.findViewById(R.id.edit_profileImage);
        btnStartChat = view.findViewById(R.id.startChatBtn);
        btnSend = view.findViewById(R.id.sendBtn);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                auth.signOut();
//                ((ReplaceActivity) getActivity()).setFragment(new CreateAccountFragment());
            }
        });

    }
    private void loadDataBasic() {
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e("Tag_0", error.getMessage());
                    return;
                }

                assert value != null;
                if (value.exists()){
                    String name = value.getString("name");
                    String status = value.getString("status");
                    final String profileURL = value.getString("profileImage");
                    int following = value.getLong("following").intValue();
                    int followers = value.getLong("followers").intValue();
//                    String followers = value.getString("followers");

                    tvName.setText(name);
                    tvToolbarName.setText(name);
                    tvStatus.setText(status);
                    tvFollowersCount.setText(String.valueOf(followers));
                    tvFollowingCount.setText(String.valueOf(following));

                    Glide.with(getContext().getApplicationContext())
                            .load(profileURL)
                            .timeout(6500)
                            .placeholder(R.drawable.ic_person)
                            .into(cimgProfileImage);

                }
            }
        });
    }
    private static class  PostImageHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;


        public PostImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
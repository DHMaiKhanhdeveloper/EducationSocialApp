package com.example.SocialMedia1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.SocialMedia1.Fragments.FavouriteFragment;
import com.example.SocialMedia1.Fragments.FeedFragment;
import com.example.SocialMedia1.Fragments.FollowingFragment;
import com.example.SocialMedia1.Fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import SocialMedia1.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    FirebaseAuth auth;
    FirebaseUser user;

    CircleImageView profile_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        frameLayout=findViewById(R.id.frameLayout);
        bottomNavigationView=findViewById(R.id.bottom_nav);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();








        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.following) {
                    FollowingFragment followingFragment = new FollowingFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, followingFragment);
                    fragmentTransaction.commit();
                } else if (id == R.id.feed) {
                    FeedFragment feedFragment = new FeedFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, feedFragment);
                    fragmentTransaction.commit();
                } else if (id == R.id.fav) {
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, favouriteFragment);
                    fragmentTransaction.commit();
                } else if (id == R.id.profile) {
                    UserFragment userFragment = new UserFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, userFragment);
                    fragmentTransaction.commit();
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getUid());
                    editor.apply();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.following);



    }




}
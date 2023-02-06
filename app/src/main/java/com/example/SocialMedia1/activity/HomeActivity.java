package com.example.SocialMedia1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.SocialMedia1.Fragments.CoursesFragment;
import com.example.SocialMedia1.Fragments.FavouriteFragment;
import com.example.SocialMedia1.Fragments.FeedFragment;
import com.example.SocialMedia1.Fragments.FollowingFragment;
import com.example.SocialMedia1.Fragments.UserFragment;
import com.example.SocialMedia1.viewpager.ViewPagerAdapter;
import com.example.SocialMedia1.viewpager.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import SocialMedia1.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    FirebaseAuth auth;
    FirebaseUser user;

    CircleImageView profile_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        frameLayout=findViewById(R.id.frameLayout);
        bottomNavigationView=findViewById(R.id.bottom_nav);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        createinit();



//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
////                if (id == R.id.courses) {
////                    CoursesFragment coursesFragment = new CoursesFragment();
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.frameLayout, coursesFragment);
////                    fragmentTransaction.commit();
////                }
//                if (id == R.id.feed) {
//                    FeedFragment feedFragment = new FeedFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frameLayout, feedFragment);
//                    fragmentTransaction.commit();
//                }else if (id == R.id.following) {
//                    FollowingFragment followingFragment = new FollowingFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frameLayout, followingFragment);
//                    fragmentTransaction.commit();
//                }
////                else if (id == R.id.feed) {
////                    FeedFragment feedFragment = new FeedFragment();
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.frameLayout, feedFragment);
////                    fragmentTransaction.commit();
////                }
//                else if (id == R.id.fav) {
//                    FavouriteFragment favouriteFragment = new FavouriteFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frameLayout, favouriteFragment);
//                    fragmentTransaction.commit();
//                } else if (id == R.id.profile) {
//                    UserFragment userFragment = new UserFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frameLayout, userFragment);
//                    fragmentTransaction.commit();
//                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//                    editor.putString("profileid", user.getUid());
//                    editor.apply();
//                }
//                return true;
//            }
//        });
//
//        bottomNavigationView.setSelectedItemId(R.id.feed);



    }

    private void createinit() {

//        loadFragment(new HomeFragment());

//        toolbar.setTitle("Trang chủ");
        viewPager2 = findViewById(R.id.main_view_pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.feed).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.following).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.fav).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feed:
                        viewPager2.setCurrentItem(0);
//                        toolbar.setTitle("Trang chủ");
//                        fragment = new HomeFragment();
//                        loadFragment(fragment);
                        return true;
                    case R.id.following:
                        viewPager2.setCurrentItem(1);
//                        toolbar.setTitle("Tìm kiếm");
//                        fragment = new SearchFragment();
//                        loadFragment(fragment);
//                        break;
                        return true;
                    case R.id.fav:
                        viewPager2.setCurrentItem(2);
//                        toolbar.setTitle("Thêm");
//                        fragment = new AddFragment();
//                        loadFragment(fragment);
//                        break;
                        return true;
                    case R.id.profile:
                        viewPager2.setCurrentItem(3);

                        return true;
                }
                return false;
            }
        });
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.Home:
//                        viewPager2.setCurrentItem(0);
////                        toolbar.setTitle("Trang chủ");
////                        fragment = new HomeFragment();
////                        loadFragment(fragment);
//                        return true;
//                    case R.id.Search:
//                        viewPager2.setCurrentItem(1);
////                        toolbar.setTitle("Tìm kiếm");
////                        fragment = new SearchFragment();
////                        loadFragment(fragment);
////                        break;
//                        return true;
//                    case R.id.Add:
//                        viewPager2.setCurrentItem(2);
////                        toolbar.setTitle("Thêm");
////                        fragment = new AddFragment();
////                        loadFragment(fragment);
////                        break;
//                        return true;
//                    case R.id.Notification:
//                        viewPager2.setCurrentItem(3);
//
//                        return true;
//                    case R.id.Profile:
//                        viewPager2.setCurrentItem(4);
//
//                        return true;
//
//
//                }
//                return false;
//            }
//        });


    }






}
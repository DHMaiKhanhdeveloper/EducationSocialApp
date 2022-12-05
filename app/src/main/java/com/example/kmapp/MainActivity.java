package com.example.kmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kmapp.fragments.AddFragment;
import com.example.kmapp.fragments.HomeFragment;
import com.example.kmapp.fragments.NotificaionFragment;
import com.example.kmapp.fragments.ProfileFragment;
import com.example.kmapp.fragments.SearchFragment;
import com.example.kmapp.viewpager.ViewPagerAdapter;
import com.example.kmapp.viewpager.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createinit();

    }

    private void createinit() {

//        loadFragment(new HomeFragment());

//        toolbar.setTitle("Trang chủ");
        viewPager2 = findViewById(R.id.main_view_pager);
        bottomNavigationView = findViewById(R.id.bottomnavigation);

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
                        bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.Add).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.Notification).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(true);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        viewPager2.setCurrentItem(0);
//                        toolbar.setTitle("Trang chủ");
//                        fragment = new HomeFragment();
//                        loadFragment(fragment);
                        return true;
                    case R.id.Search:
                        viewPager2.setCurrentItem(1);
//                        toolbar.setTitle("Tìm kiếm");
//                        fragment = new SearchFragment();
//                        loadFragment(fragment);
//                        break;
                        return true;
                    case R.id.Add:
                        viewPager2.setCurrentItem(2);
//                        toolbar.setTitle("Thêm");
//                        fragment = new AddFragment();
//                        loadFragment(fragment);
//                        break;
                        return true;
                    case R.id.Notification:
                        viewPager2.setCurrentItem(3);
//                        toolbar.setTitle("Thông báo");
//                        fragment = new NotificaionFragment();
//                        loadFragment(fragment);
//                        break;
                        return true;
                    case R.id.Profile:
                        viewPager2.setCurrentItem(4);
//                        toolbar.setTitle("Hồ sơ");
//                        fragment = new ProfileFragment();
//                        loadFragment(fragment);
//                        break;
                        return true;


                }
                return false;
            }
        });


    }
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.main_view_pager, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }






}
package com.example.kmapp.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kmapp.fragments.AddFragment;
import com.example.kmapp.fragments.HomeFragment;
import com.example.kmapp.fragments.NotificaionFragment;
import com.example.kmapp.fragments.ProfileFragment;
import com.example.kmapp.fragments.SearchFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {



    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

//    public ViewPagerAdapter(@NonNull Fragment fragment,int amountOfTabs) {
//        super(fragment);
//
//    }
//
//    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//        super(fragmentManager, lifecycle);
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new AddFragment();
            case 3:
                return new NotificaionFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

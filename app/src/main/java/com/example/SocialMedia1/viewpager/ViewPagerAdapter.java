package com.example.SocialMedia1.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.SocialMedia1.Fragments.FavouriteFragment;
import com.example.SocialMedia1.Fragments.FeedFragment;
import com.example.SocialMedia1.Fragments.FollowingFragment;
import com.example.SocialMedia1.Fragments.UserFragment;


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
                return new FeedFragment();
            case 1:
                return new FollowingFragment();
            case 2:
                return new FavouriteFragment();
            case 3:
                return new UserFragment();
            default:
                return new FeedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

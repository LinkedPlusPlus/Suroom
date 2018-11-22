package com.oss.android.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oss.android.Fragment.Group.*;

public class GroupPagerAdapter  extends FragmentStatePagerAdapter {

    private int count;
    public GroupPagerAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.count = tabcount;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                HomeFragment home = new HomeFragment();
                return home;
            case 1:
                PlannerFragment planner = new PlannerFragment();
                return planner;
            case 2:
                AlbumFragment album = new AlbumFragment();
                return album;
            case 3:
                ChatFragment chat = new ChatFragment();
                return chat;
            case 4:
                SettingFragment setting = new SettingFragment();
                return setting;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}

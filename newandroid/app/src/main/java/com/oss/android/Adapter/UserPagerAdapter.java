package com.oss.android.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oss.android.Fragment.User.HomeFragment;
import com.oss.android.Fragment.User.MyGroupFragment;
import com.oss.android.Fragment.User.SearchListFragment;
import com.oss.android.Fragment.User.SettingFragment;
import com.oss.android.Fragment.User.*;

public class UserPagerAdapter extends FragmentPagerAdapter {

    private int count;
    public UserPagerAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.count = tabcount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MyGroupFragment();
            case 2:
                return new SearchListFragment();
            case 3:
                return new SettingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}

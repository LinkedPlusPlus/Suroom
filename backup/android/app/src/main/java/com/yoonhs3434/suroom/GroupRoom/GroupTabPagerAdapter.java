package com.yoonhs3434.suroom.GroupRoom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GroupTabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public GroupTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                GroupHome tab1 = new GroupHome();
                return tab1;
            case 1:
                GroupPlanner tab2 = new GroupPlanner();
                return tab2;
            case 2:
                GroupAlbum tab3 = new GroupAlbum();
                return tab3;
            case 3:
                GroupChat tab4 = new GroupChat();
                return tab4;
            case 4:
                GroupSetting tab5 = new GroupSetting();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

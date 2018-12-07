package com.yoonhs3434.suroom.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomePgerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public HomePgerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                Home tab1 = new Home();
                return tab1;
            case 1:
                MyGroup tab2 = new MyGroup();
                return tab2;
            case 2:
                SearchGroup tab3 = new SearchGroup();
                return tab3;
            case 3:
                MainSetting tab4 = new MainSetting();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

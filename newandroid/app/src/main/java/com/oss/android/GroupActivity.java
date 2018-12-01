package com.oss.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.oss.android.Model.GroupModel;
import com.oss.android.Service.Adapter.GroupPagerAdapter;

public class GroupActivity extends AppCompatActivity {

    private GroupModel groupModel;
    private int groupID;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private GroupPagerAdapter pagerAdapter;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        activity = GroupActivity.this;
        Intent intent = getIntent();
        groupID = intent.getIntExtra("id", 0);
        Log.d("GroupActivity, groupID", Integer.toString(groupID));
        groupModel = new GroupModel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.group_toolbar);
        }
        tabLayout = (TabLayout) findViewById(R.id.group_tablayout);
        viewPager = (ViewPager)findViewById(R.id.group_viewpager);

        pagerAdapter = new GroupPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public GroupModel getGroupModel() {
        return groupModel;
    }
}

package com.yoonhs3434.suroom.GroupRoom;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;
import com.yoonhs3434.suroom.myLibrary.GroupObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GroupRoom extends AppCompatActivity {

    public GroupObject groupObj;
    public int groupId;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_room);

        mActivity = GroupRoom.this;

        Intent intent = getIntent();
        groupId = intent.getIntExtra("id", 0);
        Log.d("activity groupid", Integer.toString(groupId));
        groupObj = new GroupObject();

        Toolbar toolbar = (Toolbar) findViewById(R.id.groupToolbar);
        tabLayout = (TabLayout) findViewById(R.id.groupTabLayout);
        viewPager = (ViewPager) findViewById(R.id.groupViewPager);
        GroupTabPagerAdapter pagerAdapter = new GroupTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
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
}

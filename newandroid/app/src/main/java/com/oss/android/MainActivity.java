package com.oss.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.oss.android.Model.Setting;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_join;
    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        btn_login = (Button) findViewById(R.id.main_btn_login);
        btn_join = (Button) findViewById(R.id.main_btn_join);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        //Setting.setUrl("http://172.30.1.47:8000/rest/");
        Setting.setUrl("http://172.17.100.2:8000/rest/");
        mainActivity = MainActivity.this;
    }
}

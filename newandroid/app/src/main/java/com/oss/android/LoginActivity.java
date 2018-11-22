package com.oss.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oss.android.Model.Setting;
import com.oss.android.Service.Http.HttpLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText edit_id, edit_password;
    Button btn_subimt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit_id = (EditText) findViewById(R.id.login_edit_id);
        edit_password = (EditText) findViewById(R.id.login_edit_password);
        btn_subimt= (Button) findViewById(R.id.login_btn_submit);

        btn_subimt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit_id.getText().toString().equals("") || edit_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "입력을 확인해주세요.", Toast.LENGTH_LONG).show();
                    return ;
                }

                JSONObject requestData = new JSONObject();
                try {
                    requestData.accumulate("auth_id", edit_id.getText().toString());
                    requestData.accumulate("auth_pw", edit_password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpLogin send = new HttpLogin(Setting.getUrl() + "sign_in/", requestData);
                send.start();
                try {
                    send.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject result = send.getResult();
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "로그인 실패.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Setting.setUserId(result.getInt("id"));
                        Setting.setName(result.getString("auth_id")); // 이 edit_id는 Username임
                        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(intent);
                        MainActivity.mainActivity.finish();
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }
}

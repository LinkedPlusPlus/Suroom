package com.yoonhs3434.suroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yoonhs3434.suroom.home.HomeActivity;
import com.yoonhs3434.suroom.myLibrary.HttpLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    String text_id, text_pw;
    int id_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        text_id = null;
        text_pw = null;

        Button btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                text_id = ((EditText) findViewById(R.id.text_id)).getText().toString();
                text_pw = ((EditText) findViewById(R.id.text_pw)).getText().toString();

                JSONObject requestData = new JSONObject();
                try {
                    requestData.accumulate("auth_id", text_id);
                    requestData.accumulate("auth_pw", text_pw);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpLogin send = new HttpLogin(MySetting.getMyUrl()+"sign_in/", requestData);
                send.start();
                try {
                    send.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject result = send.getData();
                if(result == null){
                    id_number = -1;
                    Toast.makeText(getApplicationContext(), "로그인 실패.", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        id_number = result.getInt("id");
                        MySetting.setMyId(id_number);
                        MySetting.setMyName(text_id);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        MainActivity mainActivity = (MainActivity)MainActivity._MainActivity;
                        mainActivity.finish();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

}

package com.yoonhs3434.suroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.yoonhs3434.suroom.myLibrary.HttpPost;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    String name_string, id_string, pw_string, pw_chk_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join);
    }

    public void button_join(View v){
        id_string = ((EditText) findViewById(R.id.text_id)).getText().toString();
        pw_string = ((EditText) findViewById(R.id.text_pw)).getText().toString();
        pw_chk_string = ((EditText) findViewById(R.id.text_pw_chk)).getText().toString();


        if(pw_string.equals(pw_chk_string)){
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.accumulate("auth_id", id_string);
                jsonParam.accumulate("auth_pw", pw_string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), jsonParam.toString(), Toast.LENGTH_LONG).show();

            HttpPost send = new HttpPost(MySetting.getMyUrl()+"join/", jsonParam);

            send.start();
            try {
                send.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "정상적으로 회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
        }



    }
}

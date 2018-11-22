package com.oss.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oss.android.Model.Setting;
import com.oss.android.Service.Http.HttpPost;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    EditText id, password, password2;
    Button btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        id = (EditText) findViewById(R.id.join_edit_id);
        password = (EditText) findViewById(R.id.join_edit_password);
        password2 = (EditText) findViewById(R.id.join_edit_password2);
        btn_submit = (Button) findViewById(R.id.join_btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(password2.getText().toString())) {
                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.accumulate("auth_id", id.getText().toString());
                        jsonParam.accumulate("auth_pw", password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), jsonParam.toString(), Toast.LENGTH_LONG).show();

                    HttpPost send = new HttpPost(Setting.getUrl() + "join/", jsonParam);
                    send.start();
                    try {
                        send.join();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int result = send.getResult();
                    if (result==201) {
                        Toast.makeText(getApplicationContext(), "정상적으로 회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}

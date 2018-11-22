package com.oss.android.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.oss.android.GroupActivity;
import com.oss.android.Model.Setting;
import com.oss.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinCheck extends Activity {
    Button btn_yes, btn_no;
    int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join_check);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("group_id", 0);
        if (groupId == 0)
            finish();

        btn_yes = (Button) findViewById(R.id.joincheck_btn_yes);
        btn_no = (Button) findViewById(R.id.joincheck_btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject reqData = new JSONObject();
                try {
                    reqData.accumulate("user_id", Setting.getUserId());
                    reqData.accumulate("group_id", groupId);

                    JoinGroupTask send = new JoinGroupTask();
                    send.execute(Setting.getUrl() + "group/join/", reqData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private class JoinGroupTask extends AsyncTask<String, Void, Integer> {

        HttpURLConnection conn = null;

        @Override
        protected Integer doInBackground(String... strings) {
            String urlStr = strings[0];
            String dataStr = strings[1];

            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Context_Type", "application/json");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                Log.d("request data", dataStr);

                OutputStream os = conn.getOutputStream();
                os.write(dataStr.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 400) {
                    Log.e("join", Integer.toString(responseCode));
                }

                return responseCode;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result >= 200 && result < 400) {
                Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", groupId);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "가입 실패", Toast.LENGTH_SHORT).show();
            }
            finish();
        }


    }
}
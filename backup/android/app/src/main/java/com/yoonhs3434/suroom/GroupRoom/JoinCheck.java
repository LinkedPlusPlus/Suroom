package com.yoonhs3434.suroom.GroupRoom;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinCheck extends Activity {

    Button yesButton, noButton;
    int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join_check);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("group_id", 0);
        if(groupId == 0)
            finish();

        yesButton = (Button) findViewById(R.id.yesJoinButton);
        noButton = (Button) findViewById(R.id.noJoinButton);

    }

    public void yesButtonClicked(View v){
        JSONObject reqData = new JSONObject();
        try {
            reqData.accumulate("user_id", MySetting.getMyId());
            reqData.accumulate("group_id", groupId);

            JoinGroupTask send = new JoinGroupTask();
            send.execute(MySetting.getMyUrl()+"group/join/", reqData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void noButtonClicked(View v){
        finish();
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
                if(responseCode == 400){
                    Log.e("join", Integer.toString(responseCode));
                }

                return responseCode;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(conn!=null)
                    conn.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result >= 200 && result <400){
                Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GroupRoom.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", groupId);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "가입 실패", Toast.LENGTH_SHORT).show();
            }
            finish();
        }


    }
}

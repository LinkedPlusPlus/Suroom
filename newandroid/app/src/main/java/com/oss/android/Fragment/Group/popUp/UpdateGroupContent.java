package com.oss.android.Fragment.Group.popUp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oss.android.Model.Setting;
import com.oss.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateGroupContent extends AppCompatActivity {

    Button btn_send;
    EditText text_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group_content);

        final String category = getIntent().getStringExtra("category");

        text_content = (EditText) findViewById(R.id.textContent);
        btn_send = (Button) findViewById(R.id.buttonUpdate);
        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String content = text_content.getText().toString();
                new UpdateGroup().execute(category, content);
                finish();
            }
        });

    }

    private class UpdateGroup extends AsyncTask<String, Void, Void> {

        HttpURLConnection conn = null;

        @Override
        protected Void doInBackground(String... strings) {
            String category = strings[0];
            String content = strings[1];

            try {
                URL url = new URL(Setting.getUrl() + "group/update/"+category+"/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");

                JSONObject data = new JSONObject();
                data.accumulate("group_id", Setting.getGroupId());
                data.accumulate("content", content);
                Log.d("data", data.toString());

                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
            return null;
        }
    }
}

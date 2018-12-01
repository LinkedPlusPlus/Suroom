package com.oss.android.Fragment.Group.Planner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oss.android.Fragment.Group.PlannerFragment;
import com.oss.android.Model.Setting;
import com.oss.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PlannerCreateActivity extends AppCompatActivity {

    private EditText edit_title, edit_date, edit_content;
    private Button btn_cancel, btn_submit;
    private int user_id, group_id;
    private String year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_create);

        edit_title = (EditText) findViewById(R.id.planner_create_edit_title);
        edit_date = (EditText) findViewById(R.id.planner_create_edit_date);
        edit_content = (EditText) findViewById(R.id.planner_create_edit_content);
        btn_cancel = (Button) findViewById(R.id.planner_create_btn_cancel);
        btn_submit = (Button) findViewById(R.id.planner_create_btn_submit);

        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 0);
        group_id = intent.getIntExtra("group_id", 0);
        year = intent.getStringExtra("year");
        month = intent.getStringExtra("month");
        day = intent.getStringExtra("day");
        if (day != null)
            edit_date.setText(year + "-" + month + "-" + day);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_title.getText().toString().equals("")) {
                    Toast.makeText(PlannerCreateActivity.this, "Title을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edit_date.toString().equals("")) {
                    Toast.makeText(PlannerCreateActivity.this, "Date를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject requestData = new JSONObject();
                try {
                    requestData.accumulate("title", edit_title.getText().toString());
                    requestData.accumulate("date", edit_date.getText().toString());
                    requestData.accumulate("content", edit_content.getText().toString());
                    requestData.accumulate("user", user_id);
                    requestData.accumulate("group", group_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String[] requestStr = new String[2];
                requestStr[0] = Setting.getUrl() + "group/planner/create/";
                requestStr[1] = requestData.toString();

                HttpPost send = new HttpPost();
                send.execute(requestStr);


            }
        });

    }

    private class HttpPost extends AsyncTask<String, Void, Integer> {

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

                Log.d("PlannerCreateActivity", dataStr);

                OutputStream os = conn.getOutputStream();
                os.write(dataStr.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                String result = "";

                while ((line = reader.readLine()) != null) {
                    result += line;
                }

                reader.close();

                JSONObject resData = new JSONObject(result);

                return resData.getInt("id");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == null) {
                setResult(PlannerFragment.PLANNER_FAIL);
                finish();
            } else {
                Intent intent = new Intent();
                intent.putExtra("day", edit_date.getText().toString().substring(8));
                setResult(PlannerFragment.PLANNER_OK, intent);
                finish();
            }
        }
    }

}

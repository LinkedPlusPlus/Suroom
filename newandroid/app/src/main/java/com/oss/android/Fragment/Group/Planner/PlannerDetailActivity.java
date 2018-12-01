package com.oss.android.Fragment.Group.Planner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oss.android.Fragment.Group.PlannerFragment;
import com.oss.android.Model.Setting;
import com.oss.android.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PlannerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_title, edit_date, edit_content;
    private Button btn_cancel, btn_submit, btn_delete;
    private int id, user, group;
    private String init_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_detail);

        edit_title = (EditText) findViewById(R.id.planner_detail_edit_title);
        edit_date = (EditText) findViewById(R.id.planner_detail_edit_date);
        edit_content = (EditText) findViewById(R.id.planner_detail_edit_content);
        btn_cancel = (Button) findViewById(R.id.planner_detail_btn_cancel);
        btn_submit = (Button) findViewById(R.id.planner_detail_btn_submit);
        btn_delete = (Button) findViewById(R.id.planner_detail_btn_delete);
        Intent intent = getIntent();
        edit_title.setText(intent.getStringExtra("title"));
        edit_date.setText(intent.getStringExtra("date"));
        init_day = intent.getStringExtra("date").substring(8);
        edit_content.setText(intent.getStringExtra("content"));
        id = intent.getIntExtra("id", 0);
        user = intent.getIntExtra("user", 0);
        group = intent.getIntExtra("group", 0);

        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.planner_detail_btn_cancel: {
                finish();
                break;
            }
            case R.id.planner_detail_btn_submit: {
                break;
            }
            case R.id.planner_detail_btn_delete: {
                new HttpDelete().execute("");
                break;
            }

        }
    }

    private class HttpDelete extends AsyncTask<String, Void, Integer>{

        HttpURLConnection conn = null;
        String REQUEST_METHOD = "DELETE";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Integer doInBackground(String... strings) {
            Integer resonseCode = null;

            try {
                URL url = new URL(Setting.getUrl() + "group/planner/"+id +"/delete");
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();
                resonseCode = conn.getResponseCode();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return resonseCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result>=200 && result<300){
                Intent intent = new Intent();
                intent.putExtra("day", init_day);
                setResult(PlannerFragment.PLANNER_OK, intent);
                finish();
            } else {
                setResult(PlannerFragment.PLANNER_FAIL);
                finish();
            }
        }
    }


}

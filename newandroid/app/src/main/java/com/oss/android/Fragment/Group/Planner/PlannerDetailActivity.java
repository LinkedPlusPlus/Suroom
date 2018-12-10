package com.oss.android.Fragment.Group.Planner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oss.android.Fragment.Group.PlannerFragment;
import com.oss.android.Model.Setting;
import com.oss.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * @author jeje (las9897@gmail.com)
 * @file com.oss.android.Fragment.Group.Planner.PlannerDetailActivity.java
 * @brief 리스트에 표시된 일정을 눌렀을 때 호출되는 액티비티 입니다. 일정을 수정하거나 삭제하는 기능이 구현되어있습니다.
 */
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

        if(user != Setting.getUserId()){
            edit_title.setFocusable(false);
            edit_title.setClickable(false);
            edit_date.setFocusable(false);
            edit_date.setClickable(false);
            edit_content.setFocusable(false);
            edit_content.setClickable(false);
            btn_submit.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }
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
                new HttpPut().execute(edit_title.getText().toString(), edit_date.getText().toString(), edit_content.getText().toString());
                break;
            }
            case R.id.planner_detail_btn_delete: {
                new HttpDelete().execute("");
                break;
            }

        }
    }

    /**
     * @brief 서버에 DELETE 요청을 하는 이너 클래스입니다. 요청이 전달되어 정상적으로 진행되면, 일정이 삭제됩니다.
     */
    private class HttpDelete extends AsyncTask<String, Void, Integer> {

        HttpURLConnection conn = null;
        String REQUEST_METHOD = "DELETE";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Integer doInBackground(String... strings) {
            Integer resonseCode = null;
            try {
                URL url = new URL(Setting.getUrl() + "group/planner/" + id + "/delete");
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
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return resonseCode;
        }


        /**
         * @brief result는 responseCode를 담고 있습니다. 200 이상 300 미만의 코드번호는 삭제 성공의 의미를 담은 코드번호이므로 요청이 성공적으로 처리되었다는 의미를 갖고있습니다. 액티비티를 종료하면서 PlannerFragment에 일정이 삭제되었다는 처리를 해야하기 때문에 day를 intent에 담아서 전달합니다.
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result >= 200 && result < 300) {
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
    /**
     * @brief 서버에 PUT 요청을 하는 이너 클래스입니다. 요청이 전달되어 정상적으로 진행되면, 일정이 변경됩니다.
     */
    private class HttpPut extends AsyncTask<String, Void, Integer> {

        HttpURLConnection conn = null;
        String REQUEST_METHOD = "PUT";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Integer doInBackground(String... strings) {
            String title = strings[0];
            String date = strings[1];
            String content = strings[2];

            Integer resonseCode = null;
            try {
                URL url = new URL(Setting.getUrl() + "group/planner/" + id + "/update");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(REQUEST_METHOD);
               // conn.setReadTimeout(READ_TIMEOUT);
                //conn.setConnectTimeout(CONNECTION_TIMEOUT);


                JSONObject data = new JSONObject();
                data.accumulate("title", title);
                data.accumulate("date", date);
                data.accumulate("content", content);
                Log.d("data", data.toString());

                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                resonseCode = conn.getResponseCode();
                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return resonseCode;
        }

        /**
         * @brief result는 responseCode를 담고 있습니다. 200 이상 300 미만의 코드번호는 변경의 성공의 의미를 담은 코드번호이므로 요청이 성공적으로 처리되었다는 의미를 갖고있습니다. 액티비티를 종료하면서 PlannerFragment에 일정이 변경되었다는 처리를 해야하기 때문에 day를 intent에 담아서 전달합니다.
         * @param result
         */
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result >= 200 && result < 300) {
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

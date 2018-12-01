package com.oss.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.oss.android.Model.Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GroupMakeActivity extends AppCompatActivity {

    public static final int NUM_OF_TAG = 5;
    private EditText editText_name, editText_description;
    private EditText[] editTexts_tags;
    private Spinner spinner_max, spinner_public;
    private Button btn_make;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_make);

        editText_name = (EditText) findViewById(R.id.groupmake_edittext_name);
        editText_description = (EditText) findViewById(R.id.groupmake_edittext_description);
        editTexts_tags = new EditText[NUM_OF_TAG];
        editTexts_tags[0] = (EditText) findViewById(R.id.groupmake_edittext_tag1);
        editTexts_tags[1] = (EditText) findViewById(R.id.groupmake_edittext_tag2);
        editTexts_tags[2] = (EditText) findViewById(R.id.groupmake_edittext_tag3);
        editTexts_tags[3] = (EditText) findViewById(R.id.groupmake_edittext_tag4);
        editTexts_tags[4] = (EditText) findViewById(R.id.groupmake_edittext_tag5);
        spinner_max = (Spinner) findViewById(R.id.groupmake_spinner_max);
        spinner_public = (Spinner) findViewById(R.id.groupmake_spinner_public);
        btn_make = (Button) findViewById(R.id.groupmake_btn_make);

        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_name.getText().toString().equals("") || editTexts_tags[0].getText().toString().equals("")){
                    Toast.makeText(GroupMakeActivity.this, "필수 입력 부분을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = editText_name.getText().toString();
                String description = editText_description.getText().toString();
                String [] tag = new String[NUM_OF_TAG];
                boolean onPublic = true;

                for(int i=0; i<NUM_OF_TAG; i++){
                    tag[i] = editTexts_tags[i].getText().toString();
                }
                int maxNumPeople = Integer.valueOf(spinner_max.getSelectedItem().toString());

                if(spinner_public.getSelectedItem().toString().equals("비공개"))
                    onPublic = false;

                JSONObject reqData = new JSONObject();
                try {
                    reqData.accumulate("name", name);
                    reqData.accumulate("description", description);
                    reqData.accumulate("public", onPublic);
                    reqData.accumulate("max_num_people", maxNumPeople);
                    reqData.accumulate("tag1", tag[0]);
                    reqData.accumulate("tag2", tag[1]);
                    reqData.accumulate("tag3", tag[2]);
                    reqData.accumulate("tag4", tag[3]);
                    reqData.accumulate("tag5", tag[4]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String [] reqStr = new String[2];
                reqStr[0] = Setting.getUrl() + "group/";
                reqStr[1] = reqData.toString();

                MakeGroupTask send = new MakeGroupTask();
                send.execute(reqStr);
            }
        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.max_num_people, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_max.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.make_public, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_public.setAdapter(adapter2);
    }

    private class MakeGroupTask extends AsyncTask<String, Void, Integer> {

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

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                String result = "";

                while((line = reader.readLine()) != null){
                    result+=line;
                }

                reader.close();

                JSONObject resData = new JSONObject(result);

                return resData.getInt("id");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally{
                if(conn!=null)
                    conn.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result){
            if(result == null){
                return;
            }
            Log.d("group ID", Integer.toString(result));

            JSONObject reqData = new JSONObject();
            try {
                reqData.accumulate("user", Setting.getUserId());
                reqData.accumulate("group_id", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JoinGroupTask send = new JoinGroupTask();
            send.execute(Setting.getUrl()+"group/join/", reqData.toString());

            finish();
        }
    }

    private class JoinGroupTask extends AsyncTask<String, Void, Void>{

        HttpURLConnection conn = null;

        @Override
        protected Void doInBackground(String... strings) {
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
                if(responseCode == 201){
                    //Toast.makeText(getApplicationContext(), "스터디 생성 완료!", Toast.LENGTH_LONG).show();
                    Log.d("join", Integer.toString(responseCode));
                }else {

                    //Toast.makeText(getApplicationContext(), "스터디 생성 실패!", Toast.LENGTH_LONG).show();
                    Log.e("join", Integer.toString(responseCode));
                }
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
    }
}

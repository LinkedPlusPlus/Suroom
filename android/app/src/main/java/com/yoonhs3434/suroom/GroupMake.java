package com.yoonhs3434.suroom;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GroupMake extends AppCompatActivity {

    int numOfTag = 5;

    EditText nameText, descriptionText;
    EditText [] tagText;
    Spinner maxSpinner, publicSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_make);

        nameText = (EditText) findViewById(R.id.makeNameText);
        descriptionText = (EditText) findViewById(R.id.makeDescriptionText);
        tagText = new EditText[numOfTag];
        tagText[0] = (EditText) findViewById(R.id.makeTag1);
        tagText[1] = (EditText) findViewById(R.id.makeTag2);
        tagText[2] = (EditText) findViewById(R.id.makeTag3);
        tagText[3] = (EditText) findViewById(R.id.makeTag4);
        tagText[4] = (EditText) findViewById(R.id.makeTag5);
        maxSpinner = (Spinner) findViewById(R.id.makeMaxSpinner);
        publicSpinner = (Spinner) findViewById(R.id.makePublicSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.max_num_people, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxSpinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.make_public, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        publicSpinner.setAdapter(adapter2);
    }

    public void makeButtonClicked(View v) {
        String name = nameText.getText().toString();
        String description = descriptionText.getText().toString();
        String [] tag = new String[numOfTag];
        for(int i=0; i<numOfTag; i++){
            tag[i] = tagText[i].getText().toString();
        }
        int maxNumPeople = Integer.valueOf(maxSpinner.getSelectedItem().toString());
        boolean onPublic = true;
        if(publicSpinner.getSelectedItem().toString().equals("비공개"))
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
        reqStr[0] = MySetting.getMyUrl() + "group/";
        reqStr[1] = reqData.toString();

        MakeGroupTask send = new MakeGroupTask();
        send.execute(reqStr);
    }

    private class MakeGroupTask extends AsyncTask<String, Void, Integer>{

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
            Log.d("group ID", Integer.toString(result));

            JSONObject reqData = new JSONObject();
            try {
                reqData.accumulate("user_id", MySetting.getMyId());
                reqData.accumulate("group_id", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JoinGroupTask send = new JoinGroupTask();
            send.execute(MySetting.getMyUrl()+"group/join/", reqData.toString());

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
                if(responseCode == 400){
                    //Toast.makeText(getApplicationContext(), "스터디 생성 실패!", Toast.LENGTH_LONG).show();
                    Log.e("join", Integer.toString(responseCode));
                }else {
                    //Toast.makeText(getApplicationContext(), "스터디 생성 완료!", Toast.LENGTH_LONG).show();
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

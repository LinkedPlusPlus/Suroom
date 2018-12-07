package com.yoonhs3434.suroom;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.yoonhs3434.suroom.home.Home;
import com.yoonhs3434.suroom.home.HomeActivity;
import com.yoonhs3434.suroom.home.SearchGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    Button choice_subject_btn, choice_tendency_btn, matching_btn;
    RecyclerView group_list;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList items;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        choice_tendency_btn = (Button) findViewById(R.id.choiceTendencyButton);
        // matching_btn = (Button) findViewById(R.id.matchingButton);
        if(MySetting.init == false) {
            new MySetting();
            MySetting.init = true;
        }
    }

    public void groupSearchClicked(View v){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void choiceTendencyClicked(View v){
        Intent intent = new Intent(getApplicationContext(), TendencyChoiceActivity.class);
        startActivity(intent);
    }

    /*
    public void matchingClicked(View v) throws JSONException {
        JSONObject req_data = new JSONObject();
        req_data.accumulate("id", MySetting.getMyId());

        HttpPost send = new HttpPost(MySetting.getMyUrl()+"find/group/", req_data);
        send.start();
        try {
            send.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */

    private class HttpGetRequest extends AsyncTask<String, Void, JSONArray> {

        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected JSONArray doInBackground(String... strings) {
            String stringUrl = strings[0];
            JSONArray result = null;
            String inputLine;
            String stringResult;
            HttpURLConnection connection = null;

            try {
                URL myUrl = new URL(stringUrl);
                connection = (HttpURLConnection) myUrl.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();

                stringResult = stringBuilder.toString();
                result = new JSONArray(stringResult);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if(connection != null)
                    connection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
        }
    }
}

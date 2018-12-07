package com.yoonhs3434.suroom.myLibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpLogin extends Thread {
    URL url;
    String data;
    JSONObject resultData;

    public HttpLogin(String url_string, JSONObject data){
        try {
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.data = data.toString();
        resultData = null;
    }

    public void run(){
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");


            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                resultData = new JSONObject(sb.toString());
            }else{
                conn.disconnect();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn != null)
                conn.disconnect();
        }
    }

    public JSONObject getData(){
        return resultData;
    }

}

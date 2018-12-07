package com.yoonhs3434.suroom.myLibrary;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpPost extends  Thread {
    URL url;
    String data;

    public HttpPost(String url_string, JSONObject data){
        try {
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.data = data.toString();
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

            int responseCode = conn.getResponseCode();

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

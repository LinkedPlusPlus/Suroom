package com.oss.android.Service.Http;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpPost extends Thread {
    private URL url;
    private JSONObject jsondata;
    private int result;

    public HttpPost(String url, JSONObject jsondata) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.jsondata = jsondata;
    }
    @Override
    public void run() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(jsondata.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            result = conn.getResponseCode();

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getResult() {
        return result;
    }
}

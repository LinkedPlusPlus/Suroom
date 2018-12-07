package com.yoonhs3434.suroom.GroupRoom;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GroupSetting extends Fragment {

    Button withDrawButton;
    int groupId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_setting, container, false);

        groupId = MySetting.getGroupId();
        withDrawButton = (Button) view.findViewById(R.id.withdrawButton);
        withDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpWithdrawGroup().execute("");
            }
        });

        return view;
    }



    private class HttpWithdrawGroup extends AsyncTask<String, Void, Integer>{

        HttpURLConnection conn = null;
        String REQUEST_METHOD = "DELETE";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Integer doInBackground(String... strings) {
            Integer responseCode = null;
            try {
                URL url = new URL(MySetting.getMyUrl() + "group/isJoin/" + MySetting.getMyId() + "/" + groupId + "/");
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();
                responseCode = conn.getResponseCode();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn!=null)
                    conn.disconnect();
            }

            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result>=200 && result<300){
                Toast.makeText(getActivity().getApplicationContext(), "탈퇴 하였습니다.", Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

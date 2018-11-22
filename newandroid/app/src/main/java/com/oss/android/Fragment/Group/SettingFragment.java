package com.oss.android.Fragment.Group;


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

import com.oss.android.Model.Setting;
import com.oss.android.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    Button btn_leave;
    int groupId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_setting, container, false);

        groupId = Setting.getGroupId();
        btn_leave = (Button) view.findViewById(R.id.group_setting_btn_leave);
        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpWithdrawGroup().execute("");
            }
        });

        return view;
    }



    private class HttpWithdrawGroup extends AsyncTask<String, Void, Integer> {

        HttpURLConnection conn = null;
        String REQUEST_METHOD = "DELETE";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Integer doInBackground(String... strings) {
            Integer responseCode = null;
            try {
                URL url = new URL(Setting.getUrl() + "group/isJoin/" + Setting.getUserId()+ "/" + groupId + "/");
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

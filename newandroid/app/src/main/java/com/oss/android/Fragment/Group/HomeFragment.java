package com.oss.android.Fragment.Group;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oss.android.GroupActivity;
import com.oss.android.Model.Setting;
import com.oss.android.R;
import com.oss.android.Service.JoinCheck;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private ImageView imageView_banner;
    private boolean isJoin;
    private Button btn_join, btn_meeting;
    private int group_id;
    private TextView textView_title, textView_notification, textView_meeting;

    private GroupActivity mActivity;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_home, container, false);

        mActivity = (GroupActivity) GroupActivity.activity;
        isJoin = false;
        btn_join = (Button) view.findViewById(R.id.group_home_btn_join);
        btn_meeting = (Button) view.findViewById(R.id.group_home_btn_meeting);
        textView_title = (TextView) view.findViewById(R.id.group_home_textview_title);
        textView_notification = (TextView) view.findViewById(R.id.group_home_textview_notification);
        textView_meeting = (TextView) view.findViewById(R.id.group_home_textview_meeting);
        imageView_banner = (ImageView) view.findViewById(R.id.group_home_imageview_banner);


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), JoinCheck.class);
                intent.putExtra("group_id", group_id);
                startActivity(intent);
            }
        });

        group_id = Setting.getGroupId();
        Picasso.get().load(R.drawable.study_banner).into(imageView_banner);

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            group_id = Setting.getGroupId();
            if (mActivity != null && mActivity.getGroupModel() != null && mActivity.getGroupModel().getId() == group_id) {
                textView_title.setText(mActivity.getGroupModel().getTitle());
                textView_notification.setText(mActivity.getGroupModel().getNotification());
                textView_meeting.setText(mActivity.getGroupModel().getMeeting());
                if (!mActivity.getGroupModel().isFlag()) { // flag is false
                    new IsJoin().execute("temp");
                }
            } else {
                new HttpGetGroupInfo().execute(Integer.toString(group_id));
            }
        }

    }


    private class IsJoin extends AsyncTask<String, Void, Integer> {
        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        HttpURLConnection conn = null;

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                URL url = new URL(Setting.getUrl() + "group/isJoin/" + Setting.getUserId() + "/" + group_id + "/");

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();
                int responseCode = conn.getResponseCode();

                return responseCode;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 200) {
                isJoin = true;
                btn_join.setVisibility(View.GONE);
                btn_meeting.setVisibility(View.VISIBLE);
                mActivity.getGroupModel().setFlag(true);
                Log.d("isJoin", Boolean.toString(isJoin));
            } else {
                isJoin = false;
                btn_join.setVisibility(View.VISIBLE);
                btn_meeting.setVisibility(View.GONE);
                mActivity.getGroupModel().setFlag(true);
                Log.d("isJoin", Boolean.toString(isJoin));
            }
        }
    }

    private class HttpGetGroupInfo extends AsyncTask<String, Void, JSONObject> {

        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;

        private String group_Id;

        HttpURLConnection conn = null;

        @Override
        protected JSONObject doInBackground(String... strings) {
            group_Id = strings[0];
            try {
                URL url = new URL(Setting.getUrl() + "group/" + group_Id + "/");

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();

                InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                streamReader.close();
                reader.close();

                String requestData = stringBuilder.toString();
                JSONObject result = new JSONObject(requestData);
                conn.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d("result", result.toString());
                try {
                    mActivity.getGroupModel().setId(result.getInt("id"));
                    mActivity.getGroupModel().setTitle(result.getString("name"));
                    mActivity.getGroupModel().setNotification(result.getString("notification"));
                    mActivity.getGroupModel().setMeeting(result.getString("meeting"));
                    textView_title.setText(result.getString("name"));
                    textView_notification.setText(result.getString("notification"));
                    textView_meeting.setText(result.getString("meeting"));

                    new IsJoin().execute("temp");
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.execute(group_Id);
                }
            }
        }
    }
}



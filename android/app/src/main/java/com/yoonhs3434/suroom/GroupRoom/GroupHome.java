package com.yoonhs3434.suroom.GroupRoom;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;
import com.yoonhs3434.suroom.myLibrary.GroupObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GroupHome extends Fragment {

    ImageView bannerImage;
    boolean isJoin;
    Button joinButton, meetingButton;
    int groupId;
    TextView nameText, notificationText, meetingText;

    GroupRoom mActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_home, container, false);

        mActivity = (GroupRoom)GroupRoom.mActivity;
        isJoin = false;
        joinButton = (Button) view.findViewById(R.id.joinButton);
        meetingButton = (Button) view.findViewById(R.id.meetingButton);
        nameText = (TextView) view.findViewById(R.id.studyNameText);
        notificationText = (TextView) view.findViewById(R.id.studyNotificationText);
        meetingText = (TextView) view.findViewById(R.id.meetingText);
        bannerImage = (ImageView) view.findViewById(R.id.bannerImage);
        joinButton.setOnClickListener(joinButtonOnClickListener);

        groupId = MySetting.getGroupId();
        Picasso.get().load(R.drawable.study_banner).into(bannerImage);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            groupId = MySetting.getGroupId();
            if(mActivity != null && mActivity.groupObj !=null && mActivity.groupObj.getId() == groupId){
                nameText.setText(mActivity.groupObj.getName());
                notificationText.setText(mActivity.groupObj.getNotification());
                meetingText.setText(mActivity.groupObj.getMeeting());
                if(mActivity.groupObj.flag == false){
                    new IsJoin().execute("temp");
                }
            }else {
                new HttpGetGroupInfo().execute(Integer.toString(groupId));
            }
        }
    }

    private View.OnClickListener joinButtonOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity().getApplicationContext(), JoinCheck.class);
            intent.putExtra("group_id", groupId);
            startActivity(intent);
        }
    };



    private class IsJoin extends AsyncTask<String, Void, Integer> {
        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        HttpURLConnection conn = null;

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                URL url = new URL(MySetting.getMyUrl()+"group/isJoin/"+MySetting.getMyId()+"/"+groupId+"/");

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
            } finally{
                if(conn!=null)
                    conn.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result == 200) {
                isJoin = true;
                joinButton.setVisibility(View.GONE);
                meetingButton.setVisibility(View.VISIBLE);
                mActivity.groupObj.flag = true;
                Log.d("isJoin", Boolean.toString(isJoin));
            }
            else{
                isJoin = false;
                joinButton.setVisibility(View.VISIBLE);
                meetingButton.setVisibility(View.GONE);
                mActivity.groupObj.flag = true;
                Log.d("isJoin", Boolean.toString(isJoin));
            }
        }


    }

    private class HttpGetGroupInfo extends AsyncTask<String, Void, JSONObject>{

        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        String groupId;

        HttpURLConnection conn = null;
        @Override
        protected JSONObject doInBackground(String... strings) {
            groupId = strings[0];
            try {
                URL url = new URL(MySetting.getMyUrl() + "group/" + groupId + "/");

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();

                InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
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
                if(conn != null)
                    conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if(result != null) {
                Log.d("result", result.toString());
                try {
                    mActivity.groupObj.setId(result.getInt("id"));
                    mActivity.groupObj.setName(result.getString("name"));
                    mActivity.groupObj.setNotification(result.getString("notification"));
                    mActivity.groupObj.setMeeting(result.getString("meeting"));
                    nameText.setText(result.getString("name"));
                    notificationText.setText(result.getString("notification"));
                    meetingText.setText(result.getString("meeting"));

                    new IsJoin().execute("temp");
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.execute(groupId);
                }
            }
        }
    }
}

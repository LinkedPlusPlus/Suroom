package com.yoonhs3434.suroom;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MySetting {
    public static final int NUM_OF_TAG = 5;
    private static String my_url;
    private static int my_id;
    private static int group_id;
    private static String my_name;
    private static User myUser;
    private static Group myGroup;
    public static boolean init = false;

    String serverUrl = "http://las9897.pythonanywhere.com/rest/";
    String localUrl = "http://10.0.2.2:8000/rest/";

    public MySetting(){
        myUser = new User();
        myGroup = new Group();
        init = true;
    }

    public static String getMyName() {
        return my_name;
    }

    public static void setMyName(String my_name) {
        MySetting.my_name = my_name;
    }    public static void setMyUrl(String urlParam){
        my_url = urlParam;
    }

    public static String getMyUrl(){
        return my_url;
    }

    public static void setMyId(int idParam){
        my_id = idParam;
    }

    public static int getMyId(){
        return my_id;
    }

    public static void setGroupId(int idParam){
        group_id = idParam;
    }

    public static int getGroupId(){
        return group_id;
    }

    public static User getMyUser() {
        return myUser;
    }

    public static Group getMyGroup() {
        return myGroup;
    }

    public static void setMyUser(User myUser) {
        MySetting.myUser = myUser;
    }

    public static void setMyGroup(Group myGroup) {
        MySetting.myGroup = myGroup;
    }

    public static void updateGroup(int groupId) {
        GetGroupDetail send = new GetGroupDetail();
        send.execute(MySetting.getMyUrl()+"group/", Integer.toString(groupId));
    }

    public static class Group{
        public int id;
        public String name;
        public String description;
        public boolean onPublic;
        public int maxNumPeople;
        public int numPeople;
        public String [] tag;
        public String created_date;

        public Group(){
            tag= new String[NUM_OF_TAG];
        }

        /*  아직 사용 안함. 언젠가는 사용할 수도?
        public Group(int id, String name, String description, boolean onPublic, int maxNumPeople, int numPeople, String[] tag, String created_date) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.onPublic = onPublic;
            this.maxNumPeople = maxNumPeople;
            this.numPeople = numPeople;
            this.tag = tag;
            this.created_date = created_date;
        }
        */
    }

    private class User{
        public String auth_id;
        public String auth_pw;

        public User(){

        }

        public User(String auth_id, String auth_pw) {
            this.auth_id = auth_id;
            this.auth_pw = auth_pw;
        }
    }



    private static class GetGroupDetail extends AsyncTask<String, Void, JSONObject>{

        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        HttpURLConnection conn = null;

        @Override
        protected JSONObject doInBackground(String... strings) {
            String urlStr = strings[0];
            String groupId = strings[1];

            String responseData;

            try {
                URL url = new URL(urlStr + groupId);

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();

                InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                String line;

                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                streamReader.close();

                responseData = stringBuilder.toString();
                JSONObject result = new JSONObject(responseData);

                Log.d("response data", result.toString());

                return result;
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
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if(jsonObject == null){
                return;
            }

            try {
                myGroup.id = jsonObject.getInt("id");
                myGroup.name = jsonObject.getString("name");
                myGroup.description = jsonObject.getString("description");
                myGroup.onPublic = jsonObject.getBoolean("public");
                myGroup.maxNumPeople = jsonObject.getInt("max_num_people");
                myGroup.numPeople = jsonObject.getInt("num_people");
                for(int i=0; i< myGroup.tag.length; i++){
                    myGroup.tag[i] = jsonObject.getString("tag"+(i+1));
                }
                myGroup.created_date = jsonObject.getString("created_date");

                Log.d("Group Status updated", myGroup.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

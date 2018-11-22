package com.oss.android.Fragment.User;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.oss.android.GroupActivity;
import com.oss.android.Model.GroupModel;
import com.oss.android.Model.Setting;
import com.oss.android.R;
import com.oss.android.Service.ViewHolder.GroupListViewHolder;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<GroupListViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GroupModel> groupList;
    private Context context;

    public MyGroupFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_mygroup, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.user_mygroup_recycleview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        groupList = new ArrayList<>();
        context = getActivity().getApplicationContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            String params = Setting.getUrl() + "group/u/" + Setting.getUserId() + "/";

            HttpGetRequest httpGetRequest = new HttpGetRequest();
            httpGetRequest.execute(params);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<GroupListViewHolder> {
        private Context context;
        private ArrayList<GroupModel> groupList;

        private int lastPosition = -1;

        public MyAdapter(Context context, ArrayList<GroupModel> groupList) {
            this.context = context;
            this.groupList = groupList;
        }


        @NonNull
        @Override
        public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
            GroupListViewHolder viewHolder = new GroupListViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GroupListViewHolder viewHolder, final int i) {
            viewHolder.getTitle().setText(groupList.get(i).getTitle());
            viewHolder.getDescription().setText(groupList.get(i).getDescription());
            viewHolder.getNumPeople().setText(Integer.toString(groupList.get(i).getNumPeople()));
            viewHolder.getMaxNumPeople().setText(Integer.toString(groupList.get(i).getMaxNumPeolpe()));
            for (int j = 0; j < viewHolder.getTag().length; j++) {
                viewHolder.getTag()[j].setText(groupList.get(i).getTags()[j]);
            }

            // setAnimation(holder.imageView, i);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Toast.makeText(context, groupList.get(i).getId() + "" + groupList.get(i).getTitle(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.putExtra("id", groupList.get(i).getId());
                    Setting.setGroupId(groupList.get(i).getId());

                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        /*
        private void setAnimation(View viewToAnimate, int i){
            if(i > lastPosition){
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = i;
            }
        }
        */
    }

    private class HttpGetRequest extends AsyncTask<String, Void, JSONArray> {

        HttpURLConnection conn;

        String REQUEST_METHOD = "GET";
        int READ_TIMEOUT = 15000;
        int CONNECTION_TIMEOUT = 15000;

        @Override
        protected JSONArray doInBackground(String... strings) {
            String stringUrl = strings[0];
            JSONArray result = null;
            String inputLine;
            String stringResult;

            try {
                URL myUrl = new URL(stringUrl);
                conn = (HttpURLConnection) myUrl.openConnection();

                conn.setRequestMethod(REQUEST_METHOD);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);

                conn.connect();

                InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();

                stringResult = stringBuilder.toString();
                result = new JSONArray(stringResult);

                Log.d("Url", stringUrl);
                Log.d("Result", result.toString());

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
            return result;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);

            if (result != null) {
                groupList = null;
                groupList = new ArrayList<>();

                int id;
                String title;
                String description;
                int numPeople;
                int maxNumPeople;
                String[] tag = new String[Setting.NUM_OF_TAG];

                try {
                    for (int i = 0; i < result.length(); i++) {
                        id = result.getJSONObject(i).getInt("id");
                        title = result.getJSONObject(i).getString("name");
                        description = result.getJSONObject(i).getString("description");
                        numPeople = result.getJSONObject(i).getInt("num_people");
                        maxNumPeople = result.getJSONObject(i).getInt("max_num_people");
                        for (int j = 0; j < tag.length; j++) {
                            tag[j] = " ";
                            String temp = result.getJSONObject(i).getString("tag" + Integer.toString(j + 1));
                            if (temp.equals("null")) {
                                tag[j] = " ";
                            } else
                                tag[j] = "# " + temp;
                        }
                        groupList.add(new GroupModel(id, title, description, numPeople, maxNumPeople, tag));
                    }

                    adapter = new MyAdapter(context, groupList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

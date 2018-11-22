package com.oss.android.Fragment.User;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oss.android.GroupActivity;
import com.oss.android.GroupMakeActivity;
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
public class SearchListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Button btn_search, btn_make;
    private EditText editText_serach;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GroupModel> groupList;
    private Context mContext;

    public SearchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_searchlist, container, false);
        editText_serach = (EditText) view.findViewById(R.id.user_searchlist_edittext_search);
        btn_search = (Button) view.findViewById(R.id.user_serachlist_btn_search);
        btn_make = (Button) view.findViewById(R.id.user_serachlist_btn_make);
        recyclerView = (RecyclerView) view.findViewById(R.id.user_searchlist_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        groupList = new ArrayList<>();
        mContext = getActivity().getApplicationContext();
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupMakeActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            String[] params = new String[1];
            params[0] = Setting.getUrl() + "group/";

            HttpGetRequest myHttp = new HttpGetRequest();
            myHttp.execute(params);

        }
    }

    @Override
    public void onRefresh() {

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
            GroupListViewHolder viewHolder = new GroupListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GroupListViewHolder viewHolder, final int i) {
            viewHolder.getTitle().setText(groupList.get(i).getTitle());
            viewHolder.getDescription().setText(groupList.get(i).getDescription());
            viewHolder.getNumPeople().setText(Integer.toString(groupList.get(i).getNumPeople()));
            viewHolder.getMaxNumPeople().setText(Integer.toString(groupList.get(i).getMaxNumPeolpe()));
            viewHolder.getMaster().setText(Integer.toString(groupList.get(i).getMasterId()));
            for (int j = 0; j < viewHolder.getTag().length; j++) {
                viewHolder.getTag()[j].setText(groupList.get(i).getTags()[j]);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Toast.makeText(context, groupList.get(i).getId() + " " + groupList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, GroupActivity.class);
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
            groupList = new ArrayList<>();

            int id;
            String title;
            String description;
            int numPeople;
            int maxNumPeople;
            int master;
            String[] tag = new String[Setting.NUM_OF_TAG];

            try {
                for (int i = 0; i < result.length(); i++) {
                    id = result.getJSONObject(i).getInt("id");
                    title = result.getJSONObject(i).getString("name");
                    description = result.getJSONObject(i).getString("description");
                    numPeople = result.getJSONObject(i).getInt("num_people");
                    maxNumPeople = result.getJSONObject(i).getInt("max_num_people");
                    master = result.getJSONObject(i).getInt("master");
                    for (int j = 0; j < tag.length; j++) {
                        tag[j] = " ";
                        String temp = result.getJSONObject(i).getString("tag" + Integer.toString(j + 1));
                        if (temp.equals("null")) {
                            tag[j] = " ";
                        } else
                            tag[j] = "# " + temp;
                    }
                    groupList.add(new GroupModel(id, title, description, numPeople, maxNumPeople, master, tag));
                }

                adapter = new MyAdapter(mContext, groupList);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.yoonhs3434.suroom.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.yoonhs3434.suroom.GroupMake;
import com.yoonhs3434.suroom.GroupMatch.ItemGroup;
import com.yoonhs3434.suroom.GroupRoom.GroupRoom;
import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

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

public class SearchGroup extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    Button searchButton, makeButton;        // 만들어야 함 (해시 태그 검색 기능)
    EditText searchText;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList items;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_group, container, false);

        searchText = (EditText) view.findViewById(R.id.searchGroupText);
        searchButton = (Button) view.findViewById(R.id.searchGroupButton);
        makeButton = (Button) view.findViewById(R.id.makeGroupButton);
        makeButton.setOnClickListener(makeOnClickListener);
        recyclerView = (RecyclerView) view.findViewById(R.id.groupListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        items = new ArrayList<ItemGroup>();
        mContext = getActivity().getApplicationContext();

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            // execute your asynctask here
            String [] params = new String[1];
            params[0] = MySetting.getMyUrl()+"group/";

            HttpGetRequest myHttp = new HttpGetRequest();
            myHttp.execute(params);
        }
    }

    @Override
    public void onRefresh() {

    }

    private View.OnClickListener makeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            Intent intent = new Intent(mContext, GroupMake.class);
            startActivity(intent);
        }
    };

    private class MyAdapter extends RecyclerView.Adapter<SearchGroup.MyAdapter.ViewHolder>{
        private Context context;
        private ArrayList<ItemGroup> mItems;

        private int lastPosition = -1;

        public MyAdapter(Context context, ArrayList mItems) {
            this.context = context;
            this.mItems = mItems;
        }

        @Override
        public SearchGroup.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_group, parent, false);
            SearchGroup.MyAdapter.ViewHolder holder = new SearchGroup.MyAdapter.ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchGroup.MyAdapter.ViewHolder holder, final int i) {
            holder.title.setText(mItems.get(i).title);
            holder.description.setText(mItems.get(i).description);
            holder.numPeople.setText(Integer.toString(mItems.get(i).numPeople));
            holder.maxNumPeople.setText(Integer.toString(mItems.get(i).maxNumPeople));
            for(int j=0; j<holder.tag.length; j++){
                holder.tag[j].setText(mItems.get(i).tags[j]);
            }

            // setAnimation(holder.imageView, i);
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Context context = v.getContext();
                    Toast.makeText(context, mItems.get(i).id +""+mItems.get(i).title, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, GroupRoom.class);
                    intent.putExtra("id", mItems.get(i).id);
                    MySetting.setGroupId(mItems.get(i).id);

                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title, description, numPeople, maxNumPeople;
            public TextView [] tag = new TextView[5];

            public ViewHolder(View v){
                super(v);
                title = (TextView) v.findViewById(R.id.textGroupTitle);
                description = (TextView) v.findViewById(R.id.textGroupDescription);
                numPeople = (TextView) v.findViewById(R.id.textNumPeople);
                maxNumPeople = (TextView) v.findViewById(R.id.textMaxNumPeople);
                tag[0] = (TextView) v.findViewById(R.id.textTag1);
                tag[1] = (TextView) v.findViewById(R.id.textTag2);
                tag[2] = (TextView) v.findViewById(R.id.textTag3);
                tag[3] = (TextView) v.findViewById(R.id.textTag4);
                tag[4] = (TextView) v.findViewById(R.id.textTag5);
            }

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
            }   finally{
                if(conn != null)
                    conn.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            items = null;
            items = new ArrayList<>();

            int id;
            String title;
            String description;
            int numPeople;
            int maxNumPeople;
            String [] tag = new String[MySetting.NUM_OF_TAG];

            try {
                for(int i=0; i<result.length(); i++){
                    id = result.getJSONObject(i).getInt("id");
                    title = result.getJSONObject(i).getString("name");
                    description = result.getJSONObject(i).getString("description");
                    numPeople = result.getJSONObject(i).getInt("num_people");
                    maxNumPeople = result.getJSONObject(i).getInt("max_num_people");
                    for(int j=0; j<tag.length; j++){
                        tag[j] = " ";
                        String temp = result.getJSONObject(i).getString("tag"+Integer.toString(j+1));
                        if(temp.equals("null")) {
                            tag[j] = " ";
                        }
                        else
                            tag[j] = "# "+temp;
                    }
                    items.add(new ItemGroup(id, title, description, maxNumPeople, numPeople, tag));
                }

                adapter = new MyAdapter(mContext, items);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

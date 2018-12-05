package com.oss.android.Fragment.Group;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.oss.android.Fragment.Group.Planner.PlannerCreateActivity;
import com.oss.android.Fragment.Group.Planner.PlannerDetailActivity;
import com.oss.android.Model.PlannerModel;
import com.oss.android.Model.Setting;
import com.oss.android.R;
import com.oss.android.Service.Adapter.GridAdapter;
import com.oss.android.View.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlannerFragment extends Fragment implements View.OnClickListener {

    public static final int PLANNER_CREATE_CODE = 1;
    public static final int PLANNER_DETAIL_CODE = 2;
    public static final int PLANNER_OK = 3;
    public static final int PLANNER_FAIL = 4;

    private String daypoint; // point
    private TextView text_date;
    private EditText editText_year, editText_month;
    private GridAdapter gridAdapter;
    private ScrollView scrollView;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    private Context mContext;

    private ImageButton imgbtn_left, imgbtn_right;
    private Button btn_add;
    private RecyclerView recyclerView;
    private ArrayList<PlannerModel> plannerList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private HttpGET httpGET;


    SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.KOREA);

    public PlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLANNER_CREATE_CODE) {
            if (resultCode == PLANNER_OK) {
                httpGET = new HttpGET(editText_year.getText().toString(), editText_month.getText().toString(), data.getStringExtra("day"));
                httpGET.execute();
            } else if (resultCode == PLANNER_FAIL) {
                Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PLANNER_DETAIL_CODE) {
            if (resultCode == PLANNER_OK) {
                httpGET = new HttpGET(editText_year.getText().toString(), editText_month.getText().toString(), data.getStringExtra("day"));
                httpGET.execute();
            } else if (resultCode == PLANNER_FAIL) {

                Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_planner, container, false);


        text_date = (TextView) view.findViewById(R.id.group_planner_text_date);
        editText_year = (EditText) view.findViewById(R.id.group_planner_edittext_year);
        editText_month = (EditText) view.findViewById(R.id.group_planner_edittext_month);

        imgbtn_left = (ImageButton) view.findViewById(R.id.group_planner_btn_left);
        imgbtn_right = (ImageButton) view.findViewById(R.id.group_planner_btn_right);
        btn_add = (Button) view.findViewById(R.id.group_planner_btn_add);
        scrollView = (ScrollView) view.findViewById(R.id.group_planner_scrollview);
        recyclerView = (RecyclerView) view.findViewById(R.id.group_planner_recyclerview);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mContext = getActivity().getApplicationContext();

        httpGET = null;

        //gridview setting
        gridView = (ExpandableHeightGridView) view.findViewById(R.id.group_planner_gridview);
        ((ExpandableHeightGridView) gridView).setExpanded(true);
        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장


        //현재 날짜 텍스트뷰에 뿌려줌
        editText_year.setText(curYearFormat.format(date));
        editText_month.setText(curMonthFormat.format(date));
        daypoint = curDayFormat.format(date);

        plannerList = new ArrayList<>();
        dayList = new ArrayList<>();
        String day = dayFormat.format(new Date());

        mCal = Calendar.getInstance();
        setCalendarDate(Integer.parseInt(editText_year.getText().toString()), Integer.parseInt(editText_month.getText().toString()) - 1);
        imgbtn_left.setOnClickListener(this);
        imgbtn_right.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        text_date.setText(editText_month.getText().toString() + "월 " + day + "일");
        httpGET = new HttpGET(Integer.parseInt(editText_year.getText().toString()), Integer.parseInt(editText_month.getText().toString()), Integer.parseInt(day));
        httpGET.execute();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!(0 <= position && position < 7) && !dayList.get(position).equals("")) {
                    daypoint = dayList.get(position);
                    text_date.setText(editText_month.getText().toString() + "월 " + daypoint + "일");
                    httpGET = new HttpGET(editText_year.getText().toString(), editText_month.getText().toString(), dayList.get(position));
                    httpGET.execute();
                }

            }
        });


        return view;
    }

    //ScrollView 이동
    public static void scrollToView(View view, final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 0);
        }
    }


    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param year
     * @param month
     */
    private void setCalendarDate(int year, int month) {
        dayList.clear();
        //gridview 요일 표시
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(year, month, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }


        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));

        }

        gridAdapter = new GridAdapter(getActivity().getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_planner_btn_left: {
                int month = Integer.parseInt(editText_month.getText().toString()) - 1;
                month--;
                if (month < 0) {
                    editText_year.setText(String.valueOf(Integer.parseInt(editText_year.getText().toString()) - 1));
                    month = 11;
                    editText_month.setText(String.valueOf(month + 1));

                } else {
                    editText_month.setText(String.valueOf(month + 1));
                }
                setCalendarDate(Integer.parseInt(editText_year.getText().toString()), month);
                break;
            }
            case R.id.group_planner_btn_right: {
                int month = Integer.parseInt(editText_month.getText().toString()) - 1;
                month++;
                if (month > 11) {
                    editText_year.setText(String.valueOf(Integer.parseInt(editText_year.getText().toString()) + 1));
                    month = 0;
                    editText_month.setText(String.valueOf(month + 1));
                } else {
                    editText_month.setText(String.valueOf(month + 1));
                }
                setCalendarDate(Integer.parseInt(editText_year.getText().toString()), month);
                break;
            }
            case R.id.group_planner_btn_add: {
                Intent intent = new Intent(mContext, PlannerCreateActivity.class);
                intent.putExtra("user_id", Setting.getUserId());
                intent.putExtra("group_id", Setting.getGroupId());
                intent.putExtra("year", editText_year.getText().toString());
                intent.putExtra("month", editText_month.getText().toString());
                if (daypoint != null && 0 < Integer.parseInt(daypoint) && Integer.parseInt(daypoint) < 10 && daypoint.length() <2)
                    daypoint = "0" + daypoint;
                intent.putExtra("day", daypoint);
                startActivityForResult(intent, PLANNER_CREATE_CODE);
            }
        }
    }

    private class PlannerListAdapter extends RecyclerView.Adapter<PlannerListViewHolder> {
        private Context context;
        private ArrayList<PlannerModel> plannerList;

        public PlannerListAdapter(Context context, ArrayList<PlannerModel> plannerList) {
            this.context = context;
            this.plannerList = plannerList;
        }

        @NonNull
        @Override
        public PlannerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planner, parent, false);
            PlannerListViewHolder viewHolder = new PlannerListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PlannerListViewHolder viewHolder, final int i) {
            viewHolder.getDate().setText(plannerList.get(i).date);
            viewHolder.getTitle().setText(plannerList.get(i).title);
            viewHolder.getId().setText(String.valueOf(plannerList.get(i).id));
            viewHolder.getUserId().setText(String.valueOf(plannerList.get(i).user));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(mContext, PlannerDetailActivity.class);
                    intent.putExtra("id", plannerList.get(i).id);
                    intent.putExtra("date", plannerList.get(i).date);
                    intent.putExtra("title", plannerList.get(i).title);
                    intent.putExtra("content", plannerList.get(i).content);
                    intent.putExtra("user", plannerList.get(i).user);
                    intent.putExtra("group", plannerList.get(i).group);
                    startActivityForResult(intent, PLANNER_DETAIL_CODE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return plannerList.size();
        }
    }

    private class PlannerListViewHolder extends RecyclerView.ViewHolder {
        private TextView text_date, text_title, text_id, text_user_id;

        public PlannerListViewHolder(@NonNull View itemView) {
            super(itemView);
            text_date = (TextView) itemView.findViewById(R.id.item_planner_textview_date);
            text_title = (TextView) itemView.findViewById(R.id.item_planner_textview_title);
            text_id = (TextView) itemView.findViewById(R.id.item_planner_textview_id);
            text_user_id = (TextView) itemView.findViewById(R.id.item_planner_textview_user_id);

        }

        public TextView getDate() {
            return text_date;
        }

        public TextView getTitle() {
            return text_title;
        }

        public TextView getId() {
            return text_id;
        }

        public TextView getUserId() {
            return text_user_id;
        }
    }

    private class HttpGET extends AsyncTask<String, Void, JSONArray> {

        private static final String REQUEST_METHOD_GET = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;

        private String group_Id;
        private String year;
        private String month;
        private String day;

        public HttpGET(int year, int month, int day) {
            this.year = String.valueOf(year);
            this.month = String.valueOf(month);
            this.day = String.valueOf(day);
        }

        public HttpGET(String year, String month, String day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        HttpURLConnection conn = null;

        @Override
        protected JSONArray doInBackground(String... strings) {
            group_Id = String.valueOf(Setting.getGroupId());
            try {
                URL url = new URL(Setting.getUrl() + "group/" + group_Id + "/planner/?year=" + year + "&month=" + month + "&day=" + day);

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(REQUEST_METHOD_GET);
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
                JSONArray result = new JSONArray(requestData);
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
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            if (result != null) {
                plannerList.clear();
                for (int i = 0; i < result.length(); i++) {
                    try {
                        PlannerModel plannerModel = new PlannerModel(
                                result.getJSONObject(i).getInt("id"), // id
                                result.getJSONObject(i).getInt("user"), // user
                                result.getJSONObject(i).getInt("group"), // group_id
                                result.getJSONObject(i).getString("date"), // date
                                result.getJSONObject(i).getString("title"), // title
                                result.getJSONObject(i).getString("content") // content
                        );
                        plannerList.add(plannerModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter = new PlannerListAdapter(mContext, plannerList);
                recyclerView.setAdapter(adapter);
                if (plannerList.size() != 0)
                    scrollToView(recyclerView, scrollView, 0);

            }
        }
    }


}


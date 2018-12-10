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
import android.view.KeyEvent;
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

/**
 * @author jeje (las9897@gmail.com)
 * @file com.oss.android.Fragment.Group.PlannerFragment
 * @brief 그룹 마다 플래너를 하나씩 가지고 있습니다. 그 플래너를 보여주는 프래그먼트로, 사용자가 달력을 조절할 수 있습니다. 일정을 추가할 때는 PlannerCreateActivity를 호출하며, 수정하거나 삭제할 때는 PlannerDetailActivity를 호출합니다.
 */
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

    /**
     * @brief requestCode가 CREATE인지 DETAIL인지 구분하여 호출한 액티비티에서 전달받은 Intent를 처리합니다. Intent에서 day를 추출하여 그 날에 해당하는 일정을 서버에 다시 요청하여 새로고침을 수행합니다.
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * @brief 처음에 사용자에게 보여줄 VIEW를 세팅하는 부분들입니다. 달력의 버튼을 눌렀을 때의 기능 등등이 구현되어있습니다.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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


        gridView = (ExpandableHeightGridView) view.findViewById(R.id.group_planner_gridview);
        ((ExpandableHeightGridView) gridView).setExpanded(true);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        editText_year.setText(curYearFormat.format(date));
        editText_month.setText(curMonthFormat.format(date));

        editText_year.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    editText_month.requestFocus();
                    return true;
                }
                return false;

            }
        });
        editText_month.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    int month = Integer.parseInt(editText_month.getText().toString()) - 1;
                    setCalendarDate(Integer.parseInt(editText_year.getText().toString()), month);
                    return true;
                }
                return false;
            }
        });

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

    /**
     * @brief 날짜를 클릭했을 때 일정이 표시되는 리스트뷰로 스크롤을 이동시켜주는 메소드입니다.
     * @param view
     * @param scrollView
     * @param count
     */
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
     * @brief year과 month를 입력하면 해당하는 달력을 가져오는 메소드입니다.
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


    /**
     * @brief 버튼 기능을 모아둔 메소드입니다. xml의 id값을 활용하였으며, 달력의 화살표 기능과 일정 추가 기능이 담겨있습니다.
     */
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
                if (daypoint != null && 0 < Integer.parseInt(daypoint) && Integer.parseInt(daypoint) < 10 && daypoint.length() < 2)
                    daypoint = "0" + daypoint;
                intent.putExtra("day", daypoint);
                startActivityForResult(intent, PLANNER_CREATE_CODE);
            }
        }
    }

    /**
     * @brief 일정 리사이클뷰에 일정 아이템들을 뿌려주는 어댑터입니다.
     */
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

    /**
     * @brief 리사이클뷰에서 findVIewById의 호출을 최소화하기 위해서 ViewHolder를 구현했습니다.
     */
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

    /**
     * @brief year, month, day를 받아서 서버에 GET 요청을 보내는 이너클래스입니다. 접근성을 생각하여 int 형을 받는 생성자와 string을 받는 생성자, 두개를 만들었습니다.
     */
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


        /**
         * @brief request가 성공적으로 처리되면 JSONArray 타입의 result를 받게됩니다. 이 result에는 요청했던 날짜에 해당하는 일정들의 데이터가 담겨있으며, 이 데이터들을 plannerList에 넣어서 리사이클뷰와 연결해줍니다.
         * @param result
         */
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


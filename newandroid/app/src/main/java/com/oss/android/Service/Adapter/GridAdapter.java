package com.oss.android.Service.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oss.android.R;
import com.oss.android.Service.ViewHolder.PlannerViewHolder;

import java.util.Calendar;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final List<String> list;
    private final LayoutInflater inflater;
    private PlannerViewHolder holder;
    public GridAdapter(Context context, List<String> list) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint ( "ResourceAsColor" )
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_calender, parent, false);
            holder = new PlannerViewHolder();

            holder.textView_item = (TextView) convertView.findViewById(R.id.item_calender_textview);

            convertView.setTag(holder);
        } else {
            holder = (PlannerViewHolder) convertView.getTag();
        }
        holder.textView_item.setText("" + getItem(position));
        //해당 날짜 텍스트 컬러,배경 변경
        Calendar mCal = Calendar.getInstance();
        //오늘 day 가져옴
        Integer today = mCal.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(today);
        if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
            holder.textView_item.setTextColor(R.color.colorPrimary);
        }
        return convertView;
    }
}

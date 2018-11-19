package com.yoonhs3434.suroom.GroupChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yoonhs3434.suroom.MySetting;
import com.yoonhs3434.suroom.R;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {


    private static final boolean USER_COMPARE_SAME = true;
    private static final boolean USER_COMPARE_DIFF = false;

    private ViewHolder viewHolder;
    private ArrayList<ChatModel> chatItemsList;
    private boolean user_compare;
    private String user_id;


    class ViewHolder {
        TextView name_other_textView;
        TextView content_other_textView;
        TextView content_my_textView;
        TextView date_other_textView;
        TextView date_my_textView;
        ImageView profile_imageView;
        LinearLayout my_user_LinearLayout;
        LinearLayout other_user_LinearLayout;

    }

    public ChatListAdapter() {

    }

    public ChatListAdapter(ArrayList<ChatModel> chatItemsList) {
        this.chatItemsList = chatItemsList;
        this.user_id = Integer.toString(MySetting.getMyId());
    }


    @Override
    public int getCount() {
        return chatItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();


       /* 위 삼항연산자의 if 문 형식
       if (position != 0) {
            if (chatItemsList.get(position).getName().equals(chatItemsList.get(position - 1).getName())) {
                user_compare = USER_COMPARE_MINE;
            } else
                user_compare = USER_COMPARE_OTHER;
        } else {
            user_compare = USER_COMPARE_NONE;
        }
        */
        // 동일 유저면 프로필 지우기


        // 동일 시간이면 시간 지우기
        String date = position != 0 ? (chatItemsList.get(position).getDate().equals(chatItemsList.get(position - 1).getDate()) ? "" : chatItemsList.get(position).getDate()) : chatItemsList.get(position).getDate();
        // GROUP 채팅방에서 USER 구분
        user_compare = position != 0 ? (chatItemsList.get(position).getName().equals(chatItemsList.get(position - 1).getName()) ? USER_COMPARE_SAME : USER_COMPARE_DIFF) : USER_COMPARE_DIFF;
        //user_compare = position > 0 ? (chatItemsList.get(position).getDate().equals(chatItemsList.get(position-1))) : chatItemsList.get(.getDate().equals()

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //findViewById를 줄이기 위한 VIewHolder
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_chat, parent, false);
            //my
            viewHolder.my_user_LinearLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_linearlayout_my_user);
            viewHolder.other_user_LinearLayout = (LinearLayout) convertView.findViewById(R.id.chat_item_linearlayout_other_user);
            viewHolder.content_my_textView = (TextView) convertView.findViewById(R.id.content_my_textView);
            viewHolder.date_my_textView = (TextView) convertView.findViewById(R.id.date_my_textView);
            //other
            viewHolder.content_other_textView = (TextView) convertView.findViewById(R.id.content_other_textView);
            viewHolder.name_other_textView = (TextView) convertView.findViewById(R.id.name_other_textview);
            viewHolder.date_other_textView = (TextView) convertView.findViewById(R.id.date_other_textView);
            viewHolder.profile_imageView = (ImageView) convertView.findViewById(R.id.profile_imageView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (user_id == chatItemsList.get(position).getId()) {
            //INVISIBLE
            viewHolder.other_user_LinearLayout.setVisibility(View.GONE);
            //VISIBLE
            viewHolder.my_user_LinearLayout.setVisibility(View.VISIBLE);
            //Set Data
            viewHolder.date_my_textView.setText(date);
            viewHolder.content_my_textView.setText(chatItemsList.get(position).getContent());
        } else {

            //INVISIBLE
            viewHolder.my_user_LinearLayout.setVisibility(View.GONE);
            //VISIBLE
            viewHolder.other_user_LinearLayout.setVisibility(View.VISIBLE);
            //Set Data
            viewHolder.date_other_textView.setText(date);
            viewHolder.content_other_textView.setText(chatItemsList.get(position).getContent());
            viewHolder.name_other_textView.setText(chatItemsList.get(position).getName());

            //profile_image setting...
            /*if (USER_COMPARE_SAME) {
                viewHolder.profile_imageView.setImageResource(android.R.color.transparent);
            } else {
                viewHolder.profile_imageView.setImageResource(chatItemsList.get(position).getImage());
            }*/
            viewHolder.profile_imageView.setImageResource(chatItemsList.get(position).getImage());
        }
        return convertView;

    }
}



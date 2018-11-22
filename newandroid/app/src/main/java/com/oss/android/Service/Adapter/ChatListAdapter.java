package com.oss.android.Service.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oss.android.Model.ChatModel;
import com.oss.android.Model.Setting;
import com.oss.android.R;
import com.oss.android.Service.ViewHolder.ChatViewHolder;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private static final boolean USER_COMPARE_SAME = true;
    private static final boolean USER_COMPARE_DIFF = false;

    private ChatViewHolder viewHolder;
    private ArrayList<ChatModel> chatList;
    private boolean user_compare;
    private String user_id;

    public ChatListAdapter() {

    }

    public ChatListAdapter(ArrayList<ChatModel> chatList) {
        this.chatList = chatList;
        this.user_id = Integer.toString(Setting.getUserId());
    }


    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();


        // 동일 시간이면 시간 지우기
        //String date = position != 0 ? (chatList.get(position).getDate().equals(chatList.get(position - 1).getDate()) ? "" : chatList.get(position).getDate()) : chatList.get(position).getDate();
        // GROUP 채팅방에서 USER 구분
        user_compare = position != 0 ? (chatList.get(position).getName().equals(chatList.get(position - 1).getName()) ? USER_COMPARE_SAME : USER_COMPARE_DIFF) : USER_COMPARE_DIFF;
        //user_compare = position > 0 ? (chatList.get(position).getDate().equals(chatList.get(position-1))) : chatList.get(.getDate().equals()

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //findViewById를 줄이기 위한 VIewHolder
            viewHolder = new ChatViewHolder();
            convertView = inflater.inflate(R.layout.item_chat, parent, false);
            //my
            viewHolder.linearLayout_my_user = (LinearLayout) convertView.findViewById(R.id.item_chat_linearlayout_my_user);
            viewHolder.linearLayout_other_user = (LinearLayout) convertView.findViewById(R.id.item_chat_linearlayout_other_user);
            viewHolder.textView_my_content = (TextView) convertView.findViewById(R.id.item_chat_textView_my_content);
            viewHolder.textView_my_date = (TextView) convertView.findViewById(R.id.item_chat_textView_my_date);
            //other
            viewHolder.textView_other_content = (TextView) convertView.findViewById(R.id.item_chat_textView_other_content);
            viewHolder.textView_other_name = (TextView) convertView.findViewById(R.id.item_chat_textview_other_name);
            viewHolder.textView_other_date = (TextView) convertView.findViewById(R.id.item_chat_textView_other_date);
            viewHolder.imageView_other_profile = (ImageView) convertView.findViewById(R.id.item_chat_other_imageView_profile);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChatViewHolder) convertView.getTag();
        }


        if (user_id.equals(chatList.get(position).getId())) {
            //INVISIBLE
            viewHolder.linearLayout_other_user.setVisibility(View.GONE);
            //VISIBLE
            viewHolder.linearLayout_my_user.setVisibility(View.VISIBLE);
            //Set Data
            viewHolder.textView_my_date.setText(chatList.get(position).getDate());
            viewHolder.textView_my_content.setText(chatList.get(position).getContent());
        } else {

            //INVISIBLE
            viewHolder.linearLayout_my_user.setVisibility(View.GONE);
            //VISIBLE
            viewHolder.linearLayout_other_user.setVisibility(View.VISIBLE);
            //Set Data
            viewHolder.textView_other_date.setText(chatList.get(position).getDate());
            viewHolder.textView_other_content.setText(chatList.get(position).getContent());
            viewHolder.textView_other_name.setText(chatList.get(position).getName());

            //profile_image setting...
            /*if (USER_COMPARE_SAME) {
                viewHolder.imageView_other_profile.setImageResource(android.R.color.transparent);
            } else {
                viewHolder.imageView_other_profile.setImageResource(chatList.get(position).getImage());
            }*/
            viewHolder.imageView_other_profile.setImageResource(chatList.get(position).getImage());
        }
        return convertView;

    }
}

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

/**
 * @author jeje (las9897@gmail.com)
 * @file com.oss.android.Service.Adapter.ChatListAdapter.java
 * @brief ChatFragment에서 사용되는 어댑터입니다. chatList를 받아와서 ChatFragment의 리스트뷰에 연결해줍니다.
 */
public class ChatListAdapter extends BaseAdapter {


    private ChatViewHolder viewHolder;
    private ArrayList<ChatModel> chatList;
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ChatViewHolder();
            convertView = inflater.inflate(R.layout.item_chat, parent, false);

            viewHolder.linearLayout_my_user = (LinearLayout) convertView.findViewById(R.id.item_chat_linearlayout_my_user);
            viewHolder.linearLayout_other_user = (LinearLayout) convertView.findViewById(R.id.item_chat_linearlayout_other_user);
            viewHolder.textView_my_content = (TextView) convertView.findViewById(R.id.item_chat_textView_my_content);
            viewHolder.textView_my_date = (TextView) convertView.findViewById(R.id.item_chat_textView_my_date);

            viewHolder.textView_other_content = (TextView) convertView.findViewById(R.id.item_chat_textView_other_content);
            viewHolder.textView_other_name = (TextView) convertView.findViewById(R.id.item_chat_textview_other_name);
            viewHolder.textView_other_date = (TextView) convertView.findViewById(R.id.item_chat_textView_other_date);
            viewHolder.imageView_other_profile = (ImageView) convertView.findViewById(R.id.item_chat_other_imageView_profile);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChatViewHolder) convertView.getTag();
        }


        if (user_id.equals(chatList.get(position).getId())) {
            viewHolder.linearLayout_other_user.setVisibility(View.GONE);
            viewHolder.linearLayout_my_user.setVisibility(View.VISIBLE);
            viewHolder.textView_my_date.setText(chatList.get(position).getDate());
            viewHolder.textView_my_content.setText(chatList.get(position).getContent());
        } else {


            viewHolder.linearLayout_my_user.setVisibility(View.GONE);
            viewHolder.linearLayout_other_user.setVisibility(View.VISIBLE);
            viewHolder.textView_other_date.setText(chatList.get(position).getDate());
            viewHolder.textView_other_content.setText(chatList.get(position).getContent());
            viewHolder.textView_other_name.setText(chatList.get(position).getName());

            viewHolder.imageView_other_profile.setImageResource(chatList.get(position).getImage());
        }
        return convertView;

    }
}

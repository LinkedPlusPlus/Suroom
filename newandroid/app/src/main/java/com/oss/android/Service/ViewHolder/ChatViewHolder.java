package com.oss.android.Service.ViewHolder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jeje (las9897@gmail.com)
 * @file com.oss.android.Service.ViewHolder.ChatViewHolder.java
 * @brief ChatListAdapter에서 사용되는 ViewHolder입니다. findViewById의 호출을 최소화시키기 위해서 만들었습니다.
 */
public class ChatViewHolder {
    public TextView textView_other_name;
    public TextView textView_other_content;
    public TextView textView_my_content;
    public TextView textView_other_date;
    public TextView textView_my_date;
    public ImageView imageView_other_profile;
    public LinearLayout linearLayout_my_user;
    public LinearLayout linearLayout_other_user;

}

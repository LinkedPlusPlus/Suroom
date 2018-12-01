package com.oss.android.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.oss.android.R;

public class GroupListViewHolder extends RecyclerView.ViewHolder {

    private TextView title, description, numPeople, maxNumPeople, correct;
    private TextView[] tag = new TextView[5];

    public GroupListViewHolder(@NonNull View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.item_group_textview_grouptitle);
        description = (TextView) v.findViewById(R.id.item_group_textview_description);
        numPeople = (TextView) v.findViewById(R.id.item_group_textview_numpeople);
        maxNumPeople = (TextView) v.findViewById(R.id.item_group_textview_maxnumpeople);
        tag[0] = (TextView) v.findViewById(R.id.item_group_textview_tag1);
        tag[1] = (TextView) v.findViewById(R.id.item_group_textview_tag2);
        tag[2] = (TextView) v.findViewById(R.id.item_group_textview_tag3);
        tag[3] = (TextView) v.findViewById(R.id.item_group_textview_tag4);
        tag[4] = (TextView) v.findViewById(R.id.item_group_textview_tag5);
        correct = (TextView) v.findViewById(R.id.item_group_correct);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getNumPeople() {
        return numPeople;
    }

    public TextView getMaxNumPeople() {
        return maxNumPeople;
    }

    public TextView getCorrect() { return correct; }

    public TextView[] getTag() {
        return tag;
    }
}

package com.oss.android.Model;

public class PlannerModel {

    public int id; // planner pk
    public int user;
    public int group;
    public String date;
    public String title;
    public String content;

    public PlannerModel(int id, int user, int group, String date, String title, String content) {
        this.id = id;
        this.user = user;
        this.group = group;
        this.date = date;
        this.title = title;
        this.content = content;
    }
}

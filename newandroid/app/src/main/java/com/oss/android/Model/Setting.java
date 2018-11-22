package com.oss.android.Model;

public class Setting {

    public static final int NUM_OF_TAG = 5;

    private static String url;
    private static int userId;
    private static String name;
    private static int groupId; // 이게 필요한가?


    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Setting.url = url;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Setting.userId = userId;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Setting.name = name;
    }

    public static int getGroupId() {
        return groupId;
    }

    public static void setGroupId(int groupId) {
        Setting.groupId = groupId;
    }
}

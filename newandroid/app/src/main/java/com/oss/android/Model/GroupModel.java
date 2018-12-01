package com.oss.android.Model;

public class GroupModel {
    // 특정 그룹의 정보를 가져올 떄 사용하는 모델
    private boolean flag;
    private int id;
    private String title;
    private String description;
    private int numPeople;
    private int maxNumPeolpe;
    private String[] tags;
    private String created_date;
    private String notification;
    private String meeting;
    private double[] tendency;

    public GroupModel() {
        flag = false;
        tags = new String[Setting.NUM_OF_TAG];
    }

    public GroupModel(int id, String title, String description, int numPeople, int maxNumPeolpe, String[] tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.numPeople = numPeople;
        this.maxNumPeolpe = maxNumPeolpe;
        this.tags = new String[5];
        for (int i = 0; i < tags.length; i++) {
            this.tags[i] = tags[i];
        }
    }

    public GroupModel(int id, String title, String description, int numPeople, int maxNumPeolpe, String[] tags, double... tendencies) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.numPeople = numPeople;
        this.maxNumPeolpe = maxNumPeolpe;
        this.tags = new String[5];
        for (int i = 0; i < tags.length; i++) {
            this.tags[i] = tags[i];
        }
        this.tendency = new double[6];
        for(int i=0; i<6; i++){
            this.tendency[i] = tendencies[i];
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public int getMaxNumPeolpe() {
        return maxNumPeolpe;
    }

    public void setMaxNumPeolpe(int maxNumPeolpe) {
        this.maxNumPeolpe = maxNumPeolpe;
    }

    public String[] getTags() {
        return tags;
    }

    public double[] getTendency() {
        return tendency;
    }

    public void setTags(String... tags) {
        for(int i=0; i<tags.length; i++)
            this.tags[i] = tags[i];
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }
}

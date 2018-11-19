package com.yoonhs3434.suroom.myLibrary;

import com.yoonhs3434.suroom.MySetting;

public class GroupObject {
    public boolean flag;
    public int id;
    public String name;
    public String description;
    public boolean onPublic;
    public int maxNumPeople;
    public int numPeople;
    public String [] tag;
    public String created_date;
    public String notification;
    public String meeting;

    public GroupObject(){
        tag= new String[MySetting.NUM_OF_TAG];
        flag = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOnPublic(boolean onPublic) {
        this.onPublic = onPublic;
    }

    public void setMaxNumPeople(int maxNumPeople) {
        this.maxNumPeople = maxNumPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public void setTag(String... tag) {
        for(int i=0; i<tag.length; i++){
            this.tag[i] = tag[i];
        }
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOnPublic() {
        return onPublic;
    }

    public int getMaxNumPeople() {
        return maxNumPeople;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public String[] getTag() {
        return tag;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getNotification() {
        return notification;
    }

    public String getMeeting() {
        return meeting;
    }
}

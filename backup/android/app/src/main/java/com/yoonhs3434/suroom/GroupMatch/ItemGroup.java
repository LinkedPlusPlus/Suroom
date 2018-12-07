package com.yoonhs3434.suroom.GroupMatch;

public class ItemGroup {
    public int id;
    public String title;
    public String description;
    public int numPeople;
    public int maxNumPeople;
    public String[] tags;

    public ItemGroup(int id, String title, String description, int maxNumPeople, int numPeople, String... tags) {
        this.id = id;
        this.tags =  new String[5];
        this.title = title;
        this.description = description;
        this.maxNumPeople = maxNumPeople;
        this.numPeople = numPeople;
        for(int i=0; i<tags.length; i++){
            this.tags[i] = tags[i];
        }
    }

    public int getId(){
        return id;
    }
}

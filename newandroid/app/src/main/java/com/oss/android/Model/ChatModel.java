package com.oss.android.Model;

public class ChatModel {


    private String id;
    private String name;
    private String content;
    private String date;
    private int image;


    public ChatModel() {
    }

    public ChatModel(String id, String name, String content, String date, int image) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date = date;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

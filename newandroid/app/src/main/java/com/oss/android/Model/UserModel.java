package com.oss.android.Model;

public class UserModel {
    //다른 User의 데이터를 가져올 필요가 있을 때 사용
    //ex) 상대방과 1:1 채팅 등
    private int id;
    private String name;
    private double [] tendency;

    public double[] getTendency() {
        return tendency;
    }

    public void setTendency(double[] tendency) {
        this.tendency = tendency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

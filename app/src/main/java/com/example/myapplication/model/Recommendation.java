package com.example.myapplication.model;

public class Recommendation {

    private String title;
    private int imgId;

    public Recommendation(String title) {
        this.title = title;
    }

    public Recommendation(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }
    public int getImgId(){
        return imgId;
    }
}

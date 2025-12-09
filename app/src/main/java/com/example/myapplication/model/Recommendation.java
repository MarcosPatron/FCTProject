package com.example.myapplication.model;

public class Recommendation {

    private String title;
    private String description;
    private int imgId;
    private String url;

    public Recommendation(String title) {
        this.title = title;
    }

    public Recommendation(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }

    public Recommendation(String title, String description, int imgId) {
        this.title = title;
        this.description = description;
        this.imgId = imgId;
    }
    public Recommendation(String title, String description, int imgId, String url) {
        this.title = title;
        this.description = description;
        this.imgId = imgId;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImgId(){
        return imgId;
    }

    public String getUrl() {
        return url;
    }
}

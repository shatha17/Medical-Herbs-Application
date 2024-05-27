package com.example.project_2.Models;

public class plantInfoModel {

    String description;
    int imgURL;

    public plantInfoModel(String description, int imgURL) {
        this.description = description;
        this.imgURL = imgURL;
    }

    public plantInfoModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgURL() {
        return imgURL;
    }

    public void setImgURL(int imgURL) {
        this.imgURL = imgURL;
    }
}

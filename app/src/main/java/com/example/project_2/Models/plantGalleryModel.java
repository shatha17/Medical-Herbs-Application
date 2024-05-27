package com.example.project_2.Models;

public class plantGalleryModel {
    String name;
    int backgroundURL,iconURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBackgroundURL() {
        return backgroundURL;
    }

    public void setBackgroundURL(int backgroundURL) {
        this.backgroundURL = backgroundURL;
    }

    public int getIconURL() {
        return iconURL;
    }

    public void setIconURL(int iconURL) {
        this.iconURL = iconURL;
    }

    public plantGalleryModel() {
    }

    public plantGalleryModel(String name, int backgroundURL, int iconURL) {
        this.name = name;
        this.backgroundURL = backgroundURL;
        this.iconURL = iconURL;
    }
}

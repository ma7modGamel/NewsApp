package com.example.xxpc.newsapp;

public class newsModel {
    String typenews;
    String webTitle;
    String sectionName;
    String webPublicationDate;
    String webUrl;
    String nameArth;

    public newsModel(String typenews, String webTitle, String sectionName, String webPublicationDate, String webUrl, String nameArth) {
        this.typenews = typenews;
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.nameArth = nameArth;
    }

    public String getNameArth() {
        return nameArth;
    }

    public String getTypenews() {
        return typenews;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

}

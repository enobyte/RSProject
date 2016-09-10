package com.rsproject.encapsule;

/**
 * Created by Eno on 7/30/2016.
 */
public class ListItems {
    private String name;
    private String date;
    private String description;
    private String urlImage;

    public ListItems(String name, String date, String description, String urlImage) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.urlImage = urlImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


}

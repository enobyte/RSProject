package com.rsproject.encapsule;

import java.util.ArrayList;

/**
 * Created by Eno on 7/30/2016.
 */
public class ListItems {
    private String name;
    private String date;
    private String description;
    private ArrayList<String> urlImage;
    private String judul;
    private String id;


    public ListItems(String id,String name, String date, String description, ArrayList<String> urlImage, String judul) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.urlImage = urlImage;
        this.judul = judul;
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

    public ArrayList<String> getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(ArrayList<String> urlImage) {
        this.urlImage = urlImage;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

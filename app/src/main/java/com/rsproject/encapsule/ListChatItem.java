package com.rsproject.encapsule;

/**
 * Created by Eno on 7/30/2016.
 */
public class ListChatItem {
    private String name;
    private String date;
    private String content;
    private String id_laporan;

    public ListChatItem(String name, String date, String content, String id_laporan) {
        this.name = name;
        this.date = date;
        this.content = content;
        this.id_laporan = id_laporan;
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


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public void setId_laporan(String id_laporan) {
        this.id_laporan = id_laporan;
    }
}

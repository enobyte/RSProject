package com.rsproject.encapsule;

/**
 * Created by Eno on 7/30/2016.
 */
public class ListChatItem {
    private String name;
    private String date;
    private String content;
    private String level;

    public ListChatItem(String name, String date, String content, String level) {
        this.name = name;
        this.date = date;
        this.content = content;
        this.level = level;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

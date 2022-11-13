package com.example.dailynotes.Models;

import java.io.Serializable;

public class Page implements Serializable {
    private String title, description;
    private long date;
    private final int id;
    private String color;

    public Page(String title, long date, String description,int id,String color) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.id = id;
        this.color=color;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
    public long getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}

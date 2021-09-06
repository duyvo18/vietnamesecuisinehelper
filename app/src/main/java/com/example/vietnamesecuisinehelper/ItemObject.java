package com.example.vietnamesecuisinehelper;

public class ItemObject {
    private int resourceID;
    private String title;
    private  String content;

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ItemObject(int resourceID, String title, String content) {
        this.title = title;
        this.content = content;
        this.resourceID = resourceID;
    }
}

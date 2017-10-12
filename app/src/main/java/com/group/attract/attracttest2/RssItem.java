package com.group.attract.attracttest2;

/**
 * Created by nexus on 11.10.2017.
 */
public class RssItem {
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public RssItem (String title, String description){
        this.title = title;
        this.description = description;
    }

    private String title;
    private String description;
}

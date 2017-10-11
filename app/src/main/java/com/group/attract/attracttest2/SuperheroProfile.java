package com.group.attract.attracttest2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by paul on 11.10.17.
 */
public class SuperheroProfile implements Serializable {

    private String itemId;
    private String name;
    private String description;
    private String image;
    private String time;

    public SuperheroProfile(String itemId, String name, String description, String image, String time) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.time = time;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {

        return time;
    }
}

package com.example.gabri.firstapp.Model;

/**
 * Created by Gabri on 11/11/17.
 */

public class GameCover {
    private String image;
    private String description;


    public GameCover(String image, String description) {
        this.image = image;
        this.description = description;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}

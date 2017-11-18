package com.example.gabri.firstapp.Model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Gabri on 12/11/17.
 */
@Root(name="Game", strict=false)
public class Game {
    @Element(name="id",required = false)
    private int id;

    @Element(name="GameTitle",required = false)
    private String gameTitle;

    @Element(name="ReleaseDate",required = false)
    private String releaseDate;

    @Element(name="Platform",required = false)
    private String platform;
    private int thumbnail;

    public Game(String s, int i, int cover) {
        gameTitle=s;
        thumbnail=cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getThumbnail() {
        return thumbnail;
    }
}

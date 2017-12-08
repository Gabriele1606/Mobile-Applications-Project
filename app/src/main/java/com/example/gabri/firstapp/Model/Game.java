package com.example.gabri.firstapp.Model;

import com.example.gabri.firstapp.GameDetail;

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
    private String releaseDate="null";

    @Element(name="Platform",required = false)
    private String platform;
    private int thumbnail;

    private int yearOfRelease;

    private GameDetail gameDetail=null;

    private boolean gameHasFanart=false;
    private boolean gameHasBoxart=false;

    public Game(String s, int i, int cover) {
        gameTitle=s;
        thumbnail=cover;
    }


    public Game() {

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

    public boolean releaseDateIsPresent(){
        if (this.releaseDate.equals(null))
            return false;
        return true;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public GameDetail getGameDetail() {
        return gameDetail;
    }

    public void setGameDetail(GameDetail gameDetail) {
        this.gameDetail = gameDetail;
    }

    public boolean hasGameFanart() {
        return gameHasFanart;
    }

    public void setGameHasFanart(boolean gameHasFanart) {
        this.gameHasFanart = gameHasFanart;
    }

    public boolean hasGameBoxart() {
        return gameHasBoxart;
    }

    public void setGameHasBoxart(boolean gameHasBoxart) {
        this.gameHasBoxart = gameHasBoxart;
    }
}

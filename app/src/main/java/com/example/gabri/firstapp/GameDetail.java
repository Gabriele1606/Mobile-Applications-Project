package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabri on 07/12/17.
 */
@Table(database = AppDatabase.class)
@Root(name="Game", strict=false)
public class GameDetail {

    @PrimaryKey
    @Element(name = "id", required = false)
    private int id;

    @Column
    @Element(name = "GameTitle", required = false)
    private String gameTitle;

    @Column
    @Element(name = "PlatformId", required = false)
    private int platformId;

    @Column
    @Element(name = "Platform", required = false)
    private String platform;

    @Column
    @Element(name = "Overview", required = false)
    private String overView;

    //@Element(name = "genre", required = false)
    //@org.simpleframework.xml.Path("Genres")
    //private String genres;

    @Column
    @Element(name = "Players", required = false)
    private String players;

    @Column
    @Element(name = "Co-op", required = false)
    private String coop;

    @Column
    @Element(name = "Youtube", required = false)
    private String youtubeLink;

    @Column
    @Element(name = "Publisher", required = false)
    private String publisher;

    @Column
    @Element(name = "Developer", required = false)
    private String developer;

    @Column
    @Element(name = "Rating", required = false)
    private float rating;


    @Element(name = "Images", required = false)
    private Images images;



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

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

   /* public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }*/

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getCoop() {
        return coop;
    }

    public void setCoop(String coop) {
        this.coop = coop;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}



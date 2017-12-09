package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.AppDatabase;
import com.example.gabri.firstapp.Model.Platform;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Gabri on 04/12/17.
 */
@Table(database = AppDatabase.class)
@Root(name="Platform", strict=false)
public class PlatformDetail {

    @PrimaryKey
    @Element(name="id",required = false)
    private int id;

    @Column(defaultValue = "unknown")
    @Element(name="Platform",required = false)
    private String platform;



    @Column(defaultValue = "unknown")
    @Element(name="overview",required = false)
    private String overview;

    @Column(defaultValue = "unknown")
    @Element(name="developer",required = false)
    private String developer;

    @Column(defaultValue = "unknown")
    @Element(name="manufacturer",required = false)
    private String manufacturer;

    @Column(defaultValue = "unknown")
    @Element(name="cpu",required = false)
    private String cpu;

    @Column(defaultValue = "unknown")
    @Element(name="graphics",required = false)
    private String graphics;

    @Column(defaultValue = "unknown")
    @Element(name="sound",required = false)
    private String sound;

    @Column(defaultValue = "unknown")
    @Element(name="display",required = false)
    private String display;

    @Column(defaultValue = "unknown")
    @Element(name="disc",required = false)
    private String disc;

    @Column(defaultValue = "unknown")
    @Element(name="maxcontroller",required = false)
    private int maxcontroller;

    @Column(defaultValue = "0.0")
    @Element(name="Rating",required = false)
    private float rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return platform;
    }

    public void setName(String name) {
        this.platform = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public int getMaxcontroller() {
        return maxcontroller;
    }

    public void setMaxcontroller(int maxcontroller) {
        this.maxcontroller = maxcontroller;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.Platform;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Gabri on 04/12/17.
 */

@Root(name="Platform", strict=false)
public class PlatformDetail {


    @Element(name="id",required = false)

    private int id;

    @Element(name="Platform",required = false)

    private String platform;

    @Element(name="overview",required = false)

    private String overview;

    @Element(name="developer",required = false)

    private String developer;

    @Element(name="manufacturer",required = false)

    private String manufacturer;

    @Element(name="cpu",required = false)

    private String cpu;

    @Element(name="graphics",required = false)

    private String graphics;

    @Element(name="sound",required = false)

    private String sound;

    @Element(name="display",required = false)

    private String display;

    @Element(name="disc",required = false)

    private String disc;

    @Element(name="maxcontroller",required = false)

    private int maxcontroller;

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
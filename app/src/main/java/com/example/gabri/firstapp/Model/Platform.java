package com.example.gabri.firstapp.Model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Gabri on 16/11/17.
 */

@Root(name="Platform", strict=false)
public class Platform {


    @Element(name="id",required = false)
    private int id;

    @Element(name="name",required = false)
    private String name;

    @Element(name="alias",required = false)
    private String alias;

    private int averageYearOfItsGame;

    private String manufacturer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getAverageYearOfItsGame() {
        return averageYearOfItsGame;
    }

    public void setAverageYearOfItsGame(int averageYearOfItsGame) {
        this.averageYearOfItsGame = averageYearOfItsGame;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}


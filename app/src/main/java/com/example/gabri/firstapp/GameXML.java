package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Platform;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Gabri on 16/11/17.
 */

@Root(name="Data", strict = false)
public class GameXML {
    @ElementList(name="Game", inline = true)
    public List<Game> gameList;


    public List<Game> getGameList() {
    return gameList;
    }



}
package com.example.gabri.firstapp;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Gabri on 07/12/17.
 */

@Root(name="Data", strict = false)
public class GameDetailXML {

    @Element(name="Game", required = false)
    public GameDetail gameDetail=null;

    public GameDetail getGameDetail() {
        return gameDetail;
    }
}

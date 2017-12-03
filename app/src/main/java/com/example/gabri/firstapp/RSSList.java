package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RSSFeed;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Gabri on 16/11/17.
 */

@Root(name="rss", strict = false)
public class RSSList {
    @ElementList(name="item", inline = true)
    @org.simpleframework.xml.Path("channel")
    public List<RSSFeed> rssList;


    public List<RSSFeed> getRssList() {
        return rssList ;
    }

}
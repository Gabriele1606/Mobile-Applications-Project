package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Platform;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


import java.util.List;

import retrofit2.http.Path;

/**
 * Created by Gabri on 12/11/17.
 */

@Root(name="Data", strict = false)
public class PlatformXML {


    @ElementList(name="Platform", inline = true)
    @org.simpleframework.xml.Path("Platforms")
    public List<Platform> platformList;


    public List<Platform> getPlatformList() {
        return platformList;
    }

}


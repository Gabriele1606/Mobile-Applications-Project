package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.Game;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Gabri on 04/12/17.
 */

@Root(name="Data", strict = false)
public class PlatformDetailXML {
    @Element(name="Platform")
    public PlatformDetail platformDetail;


    public PlatformDetail getPlatformDetail() {
        return platformDetail;
    }

}
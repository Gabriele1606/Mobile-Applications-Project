package com.example.gabri.firstapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 03/12/2017.
 */

public class Data {
    private static List<Object> instance = new ArrayList<Object>();
    private boolean inizialized=false;
    private static Data data= new Data();
    private static List<Platform> listPlatform= new ArrayList<Platform>();
    private Data(){

    }
    public static List<Object> getInstance(){
        return instance;
    }
    public static Data getData(){
    return data;
    }
    public boolean isInizialized(){
        return inizialized;
    }
    public void setInizialized(){
        inizialized=true;
    }

    public static synchronized List<Platform> getListPlatform() {
        return listPlatform;
    }
}

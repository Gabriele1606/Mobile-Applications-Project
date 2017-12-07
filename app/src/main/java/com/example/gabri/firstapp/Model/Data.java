package com.example.gabri.firstapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 03/12/2017.
 */

public class Data {
    private static List<Object> instance = new ArrayList<Object>();
    private List<Boolean> inizialized=new ArrayList<Boolean>();
    private static Data data= new Data();
    private static List<Platform> listPlatform= new ArrayList<Platform>();
    private Data(){
        this.inizialized.add(new Boolean(false));
    }
    public static List<Object> getInstance(){
        return instance;
    }
    public static Data getData(){
    return data;
    }
    public boolean isInizialized(int id){
        return inizialized.get(id);
    }
    public void setInizialized( int id){
        inizialized.get();
    }

    public synchronized List<Platform> getListPlatform() {
        return listPlatform;
    }
}

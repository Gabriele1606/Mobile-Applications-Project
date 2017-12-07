package com.example.gabri.firstapp.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 03/12/2017.
 */

public class Data {
    private static List<Object> instance = new ArrayList<Object>();
    private Map<String,Boolean> initialized = new HashMap<>();
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
    public boolean isInitialized(String id){
        if (initialized.get(id)!=null){
            return initialized.get(id);
        }
        return false;
    }
    public void setInitialized(String id){
        initialized.put(id,new Boolean(true));
    }

    public synchronized List<Platform> getListPlatform() {
        return listPlatform;
    }
}

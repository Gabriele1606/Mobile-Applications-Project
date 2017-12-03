package com.example.gabri.firstapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 03/12/2017.
 */

public class Data {
    private static List<Object> instance = new ArrayList<Object>();

    private Data(){

    }
    public static List<Object> getInstance(){
        return instance;
    }
}

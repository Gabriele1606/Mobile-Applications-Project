package com.example.gabri.firstapp.Model;

/**
 * Created by simon on 03/12/2017.
 */

public class News {
    private String news;
    public void setText(String text){
        this.news= new String(text);
    }
    public String getText(){
        return news;
    }
}

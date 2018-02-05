package com.example.gabri.firstapp.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gabri on 23/12/17.
 */

public class User {

    private String id;
    private String username;
    private String mail;
    private String password;
    private String linkToImage;
    private String description;
    private Map<String, String> prova=new HashMap<String, String>();

    public User(String id, String username, String mail, String password, String linkToImage) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.linkToImage = linkToImage;
        prova.put("name1", "John");
        prova.put("name2", "Tim");
        prova.put("name3", "Sam");
        this.description="Personal Description, have fun!";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLinkToImage() {
        return linkToImage;
    }

    public void setLinkToImage(String linkToImage) {
        this.linkToImage = linkToImage;
    }

    /*public List<Game> getWishGameList() {
        return wishGameList;
    }

    public void setWishGameList(List<Game> wishGameList) {
        this.wishGameList = wishGameList;
    }*/
    public void setDescription(String description){
        this.description=description;
    }
    public String getDescription(){
        return description;
    }
}

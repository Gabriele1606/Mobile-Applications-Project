package com.example.gabri.firstapp.Model;

import java.util.List;

/**
 * Created by simon on 03/12/2017.
 */

public class RowGame {
    private List<Game> listGame;
    public RowGame(List<Game> listGame){
        this.listGame=listGame;
    }
    public List<Game> getList(){
        return listGame;
    }
}

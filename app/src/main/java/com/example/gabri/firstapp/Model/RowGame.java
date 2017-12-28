package com.example.gabri.firstapp.Model;

import java.util.List;

/**
 * Created by simon on 03/12/2017.
 */

public class RowGame {
    private List<Game> listGame;
    private boolean isGrid=false;
    private boolean isSlider=false;
    public RowGame(){

    }
    public RowGame(List<Game> listGame){
        this.listGame=listGame;
    }
    public List<Game> getList(){
        return listGame;
    }
    public void setSlider(boolean isSlider){
        this.isSlider=isSlider;
    }
    public boolean isSlider(){
        return isSlider;
    }
    public boolean isGrid() {
        return isGrid;
    }
    public void setGrid(boolean grid) {
        isGrid = grid;
    }
}

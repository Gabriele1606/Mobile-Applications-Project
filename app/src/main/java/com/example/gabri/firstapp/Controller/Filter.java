package com.example.gabri.firstapp.Controller;

import com.example.gabri.firstapp.Model.Game;

import java.util.List;

/**
 * Created by Gabri on 16/11/17.
 */

public class Filter {

    public Filter(){

    }

    public void getNewestGame(List<Game> gameList) {
        Game tempGame;
        int intYear;
        System.out.println("sono qui");
                for(int i=0;i<gameList.size();i++){
                    tempGame=gameList.get(i);
                    if(tempGame.getReleaseDate()==null) {
                       // gameList.remove(i);
                    }
                    else{
                        intYear = Integer.parseInt(tempGame.getReleaseDate().split("/")[2]);
                        System.out.println(intYear);
                        if (intYear < 2015) {
                            System.out.println("ho rimosso " + tempGame.getGameTitle());
                            //gameList.remove(i);
                        }
                    }


                }
        }
    }


package com.example.gabri.firstapp.Controller;

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.PlatformDetail;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Gabri on 16/11/17.
 */

public class Filter {

    public Filter(){

    }

    public void addAverageYearToPlatform(List<Platform> platformList, List<Game> gameList) {
        if(platformList.size()>0 && gameList.size()>0){
            int avg;
            int sum=0;
            int numberOfGameWithValidDate=0;
            for(int i=0;i<gameList.size();i++){
                if(!gameList.get(i).getReleaseDate().equals("null")) {
                    sum += getYearFromReleaseDate(gameList.get(i).getReleaseDate());
                    numberOfGameWithValidDate++;
                }
            }
            if(numberOfGameWithValidDate!=0) {
                avg = sum / numberOfGameWithValidDate;
            }
            else{
                avg = sum;
            }
            for(int i=0;i<platformList.size();i++){
                if(platformList.get(i).getName().equals(gameList.get(0).getPlatform()))
                    platformList.get(i).setAverageYearOfItsGame(avg);
            }

        }
    }

    public void orderListFromNewestToHolder(List<Platform> platformList){
        Collections.sort(platformList, new Comparator<Platform>() {
            @Override
            public int compare(Platform p1, Platform p2) {
                if (p1.getAverageYearOfItsGame() > p2.getAverageYearOfItsGame())
                    return 1;
                if (p1.getAverageYearOfItsGame() < p2.getAverageYearOfItsGame())
                    return -1;
                return 0;
            }
        });
    }

    public int getYearFromReleaseDate(String releaseDate){
        int year=0;
        String tmp[]=new String[2];
        tmp=releaseDate.split("/");

        switch (tmp.length){
            case 1:year=Integer.parseInt(tmp[0]);
            break;
            case 2:year=Integer.parseInt(tmp[1]);
            break;
            case 3:year=Integer.parseInt(tmp[2]);
        }
        return year;
    }

    public void setImageLink(List<RSSFeed> rssList){
        String tempDescription;
        int start;
        int end;
        for(int i=0;i<rssList.size();i++){
            tempDescription=rssList.get(i).getDescription();
            start = tempDescription.indexOf("src=\"") + 5;
            end = tempDescription.indexOf("\"", start);
            rssList.get(i).setImageLink(tempDescription.substring(start, end));
        }
    }

    public void cleanDescriptionFromHTML(List<RSSFeed> rssList){
        String tempDescription;
        int start;
        for(int i=0;i<rssList.size();i++){
            tempDescription=rssList.get(i).getDescription();
            start = tempDescription.indexOf("\n")+2;
            rssList.get(i).setDescription(tempDescription.substring(start));
        }
    }

    public String getConsoleSuitable(List<Game> gameList){
        return gameList.get(0).getPlatform();
    }

    public int averageYearOfGame(List<Game>gameList){
        String dateString;
        String arrayDate[];
        int average=0;
        for (int i=0;i<gameList.size();i++){
            dateString=gameList.get(i).getReleaseDate();
            arrayDate=dateString.split("/");
            average+=Integer.parseInt(arrayDate[2]);
        }
        System.out.println(average/gameList.size());
        return average/gameList.size();
    }


    public void addDetailsToPlatform(List<Platform> platformList, PlatformDetail platformDetail){
        for(int i=0; i<platformList.size();i++){
            if(platformDetail.getId()==platformList.get(i).getId()) {
                platformList.get(i).setPlatformDetail(platformDetail);
                System.out.println("Confronto "+ platformDetail.getName()+" con "+ platformList.get(i).getName());
            }
        }
    }

    public void addGameListPlatform(List<Platform> platformList, List<Game> gameList){
        for(int i=0; i<platformList.size();i++){
            if(gameList!=null && gameList.size()>0) {
                if (gameList.get(0).getPlatform().equals(platformList.get(i).getName())) {
                    platformList.get(i).setGameList(gameList);
                }
            }
        }
    }

}


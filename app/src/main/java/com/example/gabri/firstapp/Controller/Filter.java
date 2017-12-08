package com.example.gabri.firstapp.Controller;

import com.example.gabri.firstapp.Model.AppDatabase;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.PlatformDetail;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Gabri on 16/11/17.
 */

public class Filter {

    public Filter(){

    }

    public void addAverageYearToPlatform(final List<Platform> platformList, List<Game> gameList) {
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
            //Begin transaction to store platformList
            DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
            Transaction transaction= databaseDefinition.beginTransactionAsync(new ITransaction() {
                @Override
                public void execute(DatabaseWrapper databaseWrapper) {
                    List<Platform> platformList1 = platformList;
                    for (Platform p :
                            platformList1) {
                        FlowManager.getModelAdapter(Platform.class).save(p);
                    }
                }
            }).build();
            transaction.execute();
        }
    }

    public void orderPlatformFromNewestToHolder(List<Platform> platformList){
        Collections.sort(platformList, new Comparator<Platform>() {
            @Override
            public int compare(Platform p1, Platform p2) {
                if (p1.getAverageYearOfItsGame() < p2.getAverageYearOfItsGame())
                    return 1;
                if (p1.getAverageYearOfItsGame() > p2.getAverageYearOfItsGame())
                    return -1;
                return 0;
            }
        });
    }

    public void orderGameFromNewestToHolder(List<Game> gameList){
        int year;
        for(int i=0;i<gameList.size();i++) {
            if (!gameList.get(i).getReleaseDate().equals("null")) {
               year= getYearFromReleaseDate(gameList.get(i).getReleaseDate());
                gameList.get(i).setYearOfRelease(year);

            }
        }

        Collections.sort(gameList, new Comparator<Game>() {
            @Override
            public int compare(Game g1, Game g2) {
                if (g1.getYearOfRelease() < g2.getYearOfRelease())
                    return 1;
                if (g1.getYearOfRelease() > g2.getYearOfRelease())
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
        return average/gameList.size();
    }


    public void addDetailsToPlatform(List<Platform> platformList, final PlatformDetail platformDetail){
        for(int i=0; i<platformList.size();i++){
            if(platformDetail.getId()==platformList.get(i).getId()) {
                platformList.get(i).setPlatformDetail(platformDetail);
            }
        }
        //Begin transaction to store platformDetail
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transaction= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                    FlowManager.getModelAdapter(PlatformDetail.class).save(platformDetail);
            }
        }).build();
        transaction.execute();
    }

    public void addGameListPlatform(List<Platform> platformList, final List<Game> gameList){
        for(int i=0; i<platformList.size();i++){
            if(gameList!=null && gameList.size()>0) {
                if (gameList.get(0).getPlatform().equals(platformList.get(i).getName())) {
                    platformList.get(i).setGameList(gameList);
                    for (Game g :
                            gameList) {
                        g.setIdPlatform(platformList.get(i).getId());
                    }
                }
            }
        }

        //Begin transaction to store gameList
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transaction= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (Game g :
                        gameList) {
                    FlowManager.getModelAdapter(Game.class).save(g);
                }
            }
        }).build();
        transaction.execute();

    }

    public List<String> getDistinctDeveloperOrderedByNew(List<Platform> platformList){
        List<String> manufacturerList= new ArrayList<String>();
        String developer;
        for(int i=0;i<platformList.size();i++) {
            if(platformList.get(i).getPlatformDetail()!=null) {
                if (platformList.get(i).getPlatformDetail().getDeveloper() != null) {
                    developer = platformList.get(i).getPlatformDetail().getDeveloper();
                    if (!manufacturerList.contains(developer))
                        manufacturerList.add(developer);

                }
            }
        }

        return manufacturerList;

    }

    public List<Platform> getPlatformFromDeveloper(String developerName){
        List<Platform> allPlatform= Data.getData().getListPlatform();
        List<Platform> filteredPlatform=new ArrayList<Platform>();
        for(int i=0;i<allPlatform.size();i++){
            if(developerName.equals(allPlatform.get(i).getPlatformDetail().getDeveloper())){
                filteredPlatform.add(allPlatform.get(i));
            }
        }

        return filteredPlatform;

    }

    public List<String> getImageOfNewestGame(List<Platform> platformList){
        List<String> images=new ArrayList<String>();
        for(int i=0;i<platformList.size();i++){
            if(platformList.get(i).getGameList().size()>0 && platformList.get(i).getGameList()!=null){
                images.add("http://thegamesdb.net/banners/fanart/original/"+platformList.get(i).getGameList().get(0).getId()+"-1.jpg");
            }
        }
        return images;
    }

}


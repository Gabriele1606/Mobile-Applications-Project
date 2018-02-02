package com.example.gabri.firstapp.Controller;

import android.text.Html;

import com.example.gabri.firstapp.GameDetail;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    List<Platform> platformList1 = new ArrayList<Platform>();
                            platformList1.addAll(platformList);
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



    public void cleanDescriptionFromHTML(List<RSSFeed> rssList){
        String tempDescription;
        int start;
        for(int i=0;i<rssList.size();i++){
            tempDescription=rssList.get(i).getDescription();
            tempDescription= Html.fromHtml(tempDescription).toString();
            rssList.get(i).setDescription(tempDescription);
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
            if(allPlatform.get(i)!=null) {
                if (allPlatform.get(i).getPlatformDetail()!=null) {
                    if (developerName.equals(allPlatform.get(i).getPlatformDetail().getDeveloper())) {
                        filteredPlatform.add(allPlatform.get(i));
                    }
                }
            }
        }

        return filteredPlatform;

    }


    public void addDetailToGame(List<Platform> platformOfSpecifiedDeveloper, GameDetail gameDetail) {

        if(gameDetail!=null){
            for (int i=0;i<platformOfSpecifiedDeveloper.size();i++){
                if(gameDetail.getPlatformId()==platformOfSpecifiedDeveloper.get(i).getId()){
                    for (int j=0;j<platformOfSpecifiedDeveloper.get(i).getGameList().size();j++) {
                        if (gameDetail.getId() == platformOfSpecifiedDeveloper.get(i).getGameList().get(j).getId()) {
                            System.out.println("IMPOSTO I DETAILGAME--------------");
                            platformOfSpecifiedDeveloper.get(i).getGameList().get(j).setGameDetail(gameDetail);
                            if (gameDetail.getImages().getFanartList() != null)
                                platformOfSpecifiedDeveloper.get(i).getGameList().get(j).setGameHasFanart(true);
                            if (gameDetail.getImages().getBoxart() != null)
                                platformOfSpecifiedDeveloper.get(i).getGameList().get(j).setGameHasBoxart(true);
                        }
                    }
                }
            }
            //UPDATE DATABASE
            System.out.println("AGGIORNO I GIOCHI");
            for (Platform p :
                    platformOfSpecifiedDeveloper) {
                p.updateGame();
            }
        }
    }

    public List<String> getLinkImagesForSlider(List<Platform> platformList){
        List<String> imagesLink=new ArrayList<String>();
        int k=0;
        for(int i=0;i<platformList.size();i++){
            for(int j=0;j<platformList.get(i).getGameList().size() && k<3;j++){
                if(platformList.get(i).getGameList().get(j).hasGameFanart())
                    imagesLink.add("http://thegamesdb.net/banners/"+platformList.get(i).getGameList().get(j).getGameDetail().getImages().getFanartList().get(0).getOriginalFanart());
                    k++;

            }

        }
        return imagesLink;
    }

    public void removeImagesTagFromDescription(List<RSSFeed> rssList) {
        for(int i=0;i<rssList.size();i++){
           String tempDescription=rssList.get(i).getDescription();
            tempDescription = tempDescription.replaceAll("\\<figure.*?\\</figure>", "");
            rssList.get(i).setDescription(tempDescription);
        }
    }

    public void filterPubDate(List<RSSFeed> rssList) {
        for(int i=0;i<rssList.size();i++){
            String tempPubDate=rssList.get(i).getPubdate();
            int tempIndex=tempPubDate.indexOf("-");
            tempIndex=tempIndex-10;
            rssList.get(i).setPubdate(tempPubDate.substring(0,tempIndex));
        }
    }
}


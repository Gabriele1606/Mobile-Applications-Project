package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Game_Table;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.Platform_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabri on 09/12/17.
 */

public class DBQuery {




    public List<PlatformDetail> getPlatformDetailFromDeveloper(String developer) {
        List<PlatformDetail> platformDetailListFromDeveloper = SQLite.select().from(PlatformDetail.class).where(PlatformDetail_Table.developer.eq(developer)).queryList();
        return platformDetailListFromDeveloper;
    }

    public List<Platform> getPlatformFromPlarformDetail(List<PlatformDetail> platformDetails){
        List<Platform> platforms;
        List<Integer>platformId=new ArrayList<Integer>();
        for(int i=0;i<platformDetails.size();i++){
            platformId.add(platformDetails.get(i).getId());
        }
        platforms = SQLite.select().from(Platform.class).where(Platform_Table.id.in(platformId)).orderBy(Platform_Table.averageYearOfItsGame,false).queryList();
        return platforms;
    }

    public List<Game> getGameFromAllPlatfoms(List<Platform> platformList){
        List<Game> gameList;
        List<Integer>platformId=new ArrayList<Integer>();
        for(int i=0;i<platformList.size();i++){
            platformId.add(platformList.get(i).getId());
        }
        gameList = SQLite.select().from(Game.class).where(Game_Table.idPlatform.in(platformId)).queryList();
        return gameList;
    }
    public Platform getPlatform(String platformName){
        Platform platform = SQLite.select().from(Platform.class).where(Platform_Table.name.eq(platformName)).querySingle();
        return platform;
    }

    public List<Fanart> getFanartFromGameList(List<Game> gameList){
        List<Fanart> fanartList;
        List<Integer>gameId=new ArrayList<Integer>();
        for(int i=0;i<gameList.size();i++){
            gameId.add(gameList.get(i).getId());
        }
        fanartList=SQLite.select().from(Fanart.class).where(Fanart_Table.idGame.in(gameId)).queryList();
        return fanartList;
    }

    public List<Boxart> getBoxListArtFromGameList(List<Game> gameList){
        List<Boxart> boxartList=new ArrayList<Boxart>();
        List<Boxart> tmp;
        List<Integer>gameId=new ArrayList<Integer>();
        for(int i=0;i<gameList.size();i++){
            gameId.add(gameList.get(i).getId());
        }
        tmp=SQLite.select().from(Boxart.class).where(Boxart_Table.idGame.in(gameId)).queryList();
        for(int i=0;i<tmp.size();i++){
            if(tmp.get(i).getSide().equals("front"))
                boxartList.add(tmp.get(i));
        }
        return boxartList;
    }

    public Boxart getBoxArtFromGame(Game game){
        List<Boxart> boxartList;
        Boxart boxart=new Boxart();
        boxartList=SQLite.select().from(Boxart.class).where(Boxart_Table.idGame.eq(game.getId())).queryList();
        System.out.println(boxartList);
        if(boxartList!=null) {
            for (int i = 0; i < boxartList.size(); i++) {
                if (boxartList.get(i).getSide().equals("front"))
                    boxart = boxartList.get(i);
            }

            return boxart;
        }
        else {
            boxart.setIdGame(game.getId());
            boxart.setThumb("boxart/thumb/original/front/174-1.jpg");
            return boxart;
        }

    }


    public List<Game> getGameFromPlatfom(Platform platform) {
        List<Game> gameList;
        gameList = SQLite.select().from(Game.class).where(Game_Table.idPlatform.eq(platform.getId())).queryList();
        return gameList;
    }

    public List<Game> getGamesWithDetail(Platform platform) {
        List<Game> gameList;
        gameList = SQLite.select().from(Game.class).where(Game_Table.idPlatform.eq(platform.getId())).queryList();
        List<GameDetail> gameDetail = getGameDetail(gameList);
        List<Integer> ids= new ArrayList<Integer>();
        for (GameDetail gd :
                gameDetail) {
            ids.add(gd.getId());
        }
        gameList = SQLite.select().from(Game.class).where(Game_Table.id.in(ids)).queryList();
        return gameList;
    }

    public Game getGameFromId(int gameId) {
        Game game;
        game=SQLite.select().from(Game.class).where(Game_Table.id.eq(gameId)).querySingle();
        return game;
    }

    public GameDetail getGameDetailFromId(int gameId) {
        GameDetail gameDetail;
        gameDetail=SQLite.select().from(GameDetail.class).where(GameDetail_Table.id.eq(gameId)).querySingle();
        return gameDetail;
    }

    public List<Fanart> getFanartFromGame(Game game) {
        List<Fanart> fanart;
        fanart=SQLite.select().from(Fanart.class).where(Fanart_Table.idGame.eq(game.getId())).queryList();
        return fanart;
    }

    public List<GameDetail> getGameDetail(List<Game> games){
        List<Integer> ids= new ArrayList<Integer>();
        for (Game g :
                games) {
           ids.add(g.getId());
        }
        List<GameDetail> gameDetails;
        gameDetails= SQLite.select().from(GameDetail.class).where(GameDetail_Table.id.in(ids)).queryList();
        return gameDetails;
    }
}

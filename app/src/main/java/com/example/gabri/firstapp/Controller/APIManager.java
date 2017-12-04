package com.example.gabri.firstapp.Controller;

import android.support.v7.widget.RecyclerView;

import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Adapter.RecyclerAdapter;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.PlatformXML;
import com.example.gabri.firstapp.RSSList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by simon on 18/11/2017.
 */

public class APIManager {
    public static final String BASE_URL = "http://thegamesdb.net/api/";
    private ArrayList<Object> listObject;


    public void getPlatformList() {
        final List<List<Game>> gameListForEachPlatform = new ArrayList<List<Game>>();

        final Filter filter = new Filter();
        Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
        Call<PlatformXML> callToPlatform = possibleAPI.getPlatform();
        callToPlatform.enqueue(new Callback<PlatformXML>() {

            @Override
            public void onResponse(Call<PlatformXML> call, Response<PlatformXML> response) {

                List<Platform> platformList= response.body().getPlatformList();
                System.out.println(platformList.size());

                Call<GameXML> callToGame;
                for(int i=0;i<platformList.size();i++){
                    callToGame=possibleAPI.getGame(platformList.get(i).getName());
                    callToGame.enqueue(new Callback<GameXML>() {
                        @Override
                        public void onResponse(Call<GameXML> call, Response<GameXML> response) {
                            List<Game> gameList=response.body().getGameList();
                            // filter.getNewestGame(gameList);
                            System.out.println(gameList.size());
                            synchronized (gameListForEachPlatform) {
                                gameListForEachPlatform.add(gameList);
                            }
                        }

                        @Override
                        public void onFailure(Call<GameXML> call, Throwable t) {

                        }
                    });
                }



            }

            @Override
            public void onFailure(Call<PlatformXML> call, Throwable t) {

            }
        });
    }

    public void getRssList(List<Object> objectList, final RecyclerAdapter recyclerAdapter){
        final List<Object> list= objectList;
        final RecyclerAdapter recycler = recyclerAdapter;

        Retrofit retrofitObject = new Retrofit.Builder().baseUrl("https://multiplayer.it")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
        Call<RSSList> callToRss = possibleAPI.getRssList();
        callToRss.enqueue(new Callback<RSSList>() {
            @Override
            public void onResponse(Call<RSSList> call, Response<RSSList> response) {
                List<RSSFeed> rssList=response.body().getRssList();
                Filter filter=new Filter();
                filter.setImageLink(rssList);
                filter.cleanDescriptionFromHTML(rssList);
                list.addAll(rssList);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<RSSList> call, Throwable t) {
                System.out.println(t.getCause().toString());
            }
        });

    }

    public void getPlatformFactory(){
        final Filter filter = new Filter();


        Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
        Call<PlatformXML> callToPlatform = possibleAPI.getPlatform();
        callToPlatform.enqueue(new Callback<PlatformXML>() {

            @Override
            public void onResponse(Call<PlatformXML> call, Response<PlatformXML> response) {

                final List<Platform> platformList= response.body().getPlatformList();


                Call<GameXML> callToGame;
                for(int i=0;i<platformList.size();i++){
                    callToGame=possibleAPI.getGame(platformList.get(i).getName());
                    callToGame.enqueue(new Callback<GameXML>() {
                        @Override
                        public void onResponse(Call<GameXML> call, Response<GameXML> response) {
                            List<Game> gameList=response.body().getGameList();

                                for(int j=0;j<platformList.size();j++){
                                    if(platformList.get(j).getName().equals(gameList.get(0).getPlatform()))
                                        platformList.get(j).setAverageYearOfItsGame(filter.averageYearOfGame(gameList));
                                }

                            for (int k=0;k<platformList.size();k++){
                                System.out.println(platformList.get(k).getName());
                                System.out.println(platformList.get(k).getAverageYearOfItsGame());
                            }
                        }

                        @Override
                        public void onFailure(Call<GameXML> call, Throwable t) {
                        System.out.println("non sono riuscito");
                        }
                    });
                }



            }

            @Override
            public void onFailure(Call<PlatformXML> call, Throwable t) {
                System.out.println("non sono riuscito platform");
            }
        });

    }


}

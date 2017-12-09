package com.example.gabri.firstapp.Controller;

import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Adapter.RecyclerAdapter;
import com.example.gabri.firstapp.Adapter.SampleFragmentPagerAdapter;
import com.example.gabri.firstapp.Fanart;
import com.example.gabri.firstapp.GameDetail;
import com.example.gabri.firstapp.GameDetailXML;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.ImgSlider;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.PlatformDetailXML;
import com.example.gabri.firstapp.PlatformXML;
import com.example.gabri.firstapp.RSSList;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Integer totPlatform= new Integer(0);
    private Integer numReceivedPlatformGame = new Integer(0);
    private Integer numReceivedPlatformDetail= new Integer(0);
    private SampleFragmentPagerAdapter observer;

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


                Call<GameXML> callToGame;
                for(int i=0;i<platformList.size();i++){
                    callToGame=possibleAPI.getGame(platformList.get(i).getName());
                    callToGame.enqueue(new Callback<GameXML>() {
                        @Override
                        public void onResponse(Call<GameXML> call, Response<GameXML> response) {
                            List<Game> gameList=response.body().getGameList();
                            // filter.getNewestGame(gameList);
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
                System.out.println("non ho ricevuto console");
            }
        });
    }

    public void getGameDetail(final List<Platform> platformOfSpecifiedDeveloper, final RecyclerAdapter recyclerAdapter, final List<Object> listObject){
        int gameId;
        Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        for(int i=0;i<platformOfSpecifiedDeveloper.size();i++){
            for(int j=0;j<platformOfSpecifiedDeveloper.get(i).getGameList().size()&&j<2;j++){
                gameId=platformOfSpecifiedDeveloper.get(i).getGameList().get(j).getId();
                final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
                Call<GameDetailXML> callToGameDetail = possibleAPI.getGameDetail(gameId);
                callToGameDetail.enqueue(new Callback<GameDetailXML>() {
                    @Override
                    public void onResponse(Call<GameDetailXML> call, Response<GameDetailXML> response) {
                        GameDetail gameDetail=response.body().gameDetail;
                        //IMPOSTO ULTERIORI DATI PER IL DB E SALVO LE IMMAGINI CON FANART E BOX NEL DB
                        gameDetail.getImages().setIdGame(gameDetail.getId());
                        gameDetail.getImages().save();

                        Filter filter=new Filter();
                        synchronized (platformOfSpecifiedDeveloper) {
                            filter.addDetailToGame(platformOfSpecifiedDeveloper, gameDetail);
                            if(gameDetail!=null) {
                                if(gameDetail.getImages()!=null)
                                    if(gameDetail.getImages().getFanartList()!=null) {
                                        List<String> urlImages = ((ImgSlider) listObject.get(0)).getUrlImages();
                                        if(urlImages.size()<6) {
                                            //System.out.println("http://thegamesdb.net/banners/" + gameDetail.getImages().getFanartList().get(0).getOriginalFanart());
                                            //urlImages.add("http://thegamesdb.net/banners/" + gameDetail.getImages().getFanartList().get(0));
                                        }
                                    }

                            }

                        }

                        recyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<GameDetailXML> call, Throwable t) {
                        System.out.println(t.getCause().toString());
                        System.out.println(t.getMessage());
                    }
                });

            }
        }

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

    public void getPlatformFactory() {
        final List<Platform> platformList =new ArrayList<Platform>();



        Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
        Call<PlatformXML> callToPlatform = possibleAPI.getPlatform();
        callToPlatform.enqueue(new Callback<PlatformXML>() {

            @Override
            public void onResponse(Call<PlatformXML> call, Response<PlatformXML> response) {

                List<Platform> tempPlatformList = response.body().getPlatformList();

                Call<GameXML> callToGame;
                totPlatform= new Integer(tempPlatformList.size());
                for (int i = 0; i < tempPlatformList.size(); i++) {
                    platformList.add(tempPlatformList.get(i));
                    callToGame = possibleAPI.getGame(tempPlatformList.get(i).getName());

                    callToGame.enqueue(new Callback<GameXML>() {
                        @Override
                        public void onResponse(Call<GameXML> call, Response<GameXML> response) {
                            List<Game> gameList = response.body().getGameList();
                            Filter filter = new Filter();
                            synchronized (platformList) {
                                filter.addAverageYearToPlatform(platformList, gameList);
                                filter.addGameListPlatform(platformList, gameList);
                            }


                            synchronized (numReceivedPlatformGame){
                                numReceivedPlatformGame = numReceivedPlatformGame +1;
                                checkFinished(platformList);
                            }

                        }

                        @Override
                        public void onFailure(Call<GameXML> call, Throwable t) {
                            synchronized (numReceivedPlatformGame){
                                numReceivedPlatformGame = numReceivedPlatformGame +1;
                                checkFinished(platformList);
                            }
                        }
                    });


                    Call<PlatformDetailXML> callToPlatformDetail = possibleAPI.getPlatformDetail(platformList.get(i).getId());
                    callToPlatformDetail.enqueue(new Callback<PlatformDetailXML>() {
                        @Override
                        public void onResponse(Call<PlatformDetailXML> call, Response<PlatformDetailXML> response) {
                            Filter filter= new Filter();
                            synchronized (platformList) {
                                filter.addDetailsToPlatform(platformList, response.body().getPlatformDetail());
                            }
                            synchronized (numReceivedPlatformDetail){
                                numReceivedPlatformDetail= numReceivedPlatformDetail+1;
                                checkFinished(platformList);
                            }
                        }

                        @Override
                        public void onFailure(Call<PlatformDetailXML> call, Throwable t) {
                            synchronized (numReceivedPlatformDetail){
                                numReceivedPlatformDetail= numReceivedPlatformDetail+1;
                                checkFinished(platformList);
                            }
                        }
                    });

                }




            }

            @Override
            public void onFailure(Call<PlatformXML> call, Throwable t) {
                System.out.println("non ho ricevuto console");
            }
        });




    }

    private void checkFinished(List<Platform> platformList) {
        if (numReceivedPlatformGame.equals(totPlatform)&&numReceivedPlatformDetail.equals(totPlatform)){
            if(Data.getData().getListPlatform().isEmpty()){
                Filter filter = new Filter();
                filter.orderPlatformFromNewestToHolder(platformList);
                Data.getData().getListPlatform().addAll(platformList);
                System.out.println("HO CARICATO TUTTTI I DATI DENTRO DATA------------>"+Data.getData().getListPlatform().size());
                if (this.observer!=null)
                    this.observer.notifyDataSetChanged();
            }
        }
    }

    public void setObserver(SampleFragmentPagerAdapter observer) {
        this.observer = observer;
    }
}

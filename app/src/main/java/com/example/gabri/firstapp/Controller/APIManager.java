package com.example.gabri.firstapp.Controller;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Adapter.HorizontalAdapter;
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
import com.example.gabri.firstapp.PlatformDetail;
import com.example.gabri.firstapp.PlatformDetailXML;
import com.example.gabri.firstapp.PlatformXML;
import com.example.gabri.firstapp.RSSList;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static android.os.Build.VERSION_CODES.N;

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
    private int numPlatformInDatabase;

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
                            notifyObserver();
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

        Retrofit retrofitObject = new Retrofit.Builder().baseUrl("https://www.gamespot.com")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
        Call<RSSList> callToRss = possibleAPI.getRssList();
        callToRss.enqueue(new Callback<RSSList>() {
            @Override
            public void onResponse(Call<RSSList> call, Response<RSSList> response) {
                List<RSSFeed> rssList=response.body().getRssList();
                Filter filter=new Filter();
                filter.filterPubDate(rssList);
                filter.removeImagesTagFromDescription(rssList);
                filter.cleanDescriptionFromHTML(rssList);
                int size = list.size();
                list.addAll(rssList);
                recyclerAdapter.notifyItemRangeInserted((size+1),rssList.size());

            }

            @Override
            public void onFailure(Call<RSSList> call, Throwable t) {
            }
        });

    }

    public void getAllGameDetails(List<Platform> platformList, String developName) {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.setObserver(this.observer);
        myAsyncTask.setDevelopName(developName);
        if (platformList.size()==1) {
            System.out.println("C'è un PLATFORM UNICO");
            myAsyncTask.setMAX(40);
        }
        Data.getData().addTask(myAsyncTask);
        myAsyncTask.execute(platformList);

    }

    public void loadMoreGame(Platform platform, HorizontalAdapter horizontalAdapter){
        TaskLoadMore taskLoadMore= new TaskLoadMore();
        taskLoadMore.setObserver(horizontalAdapter);
        String platformName= platform.getName();
        taskLoadMore.setPlatformName(platformName);
        taskLoadMore.execute(platform);
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

                List<Platform> platforms = SQLite.select().from(Platform.class).queryList();
                List<PlatformDetail> platformDetails = SQLite.select().from(PlatformDetail.class).queryList();
                List<Integer> idsPlatforms= new ArrayList<Integer>();
                List<Integer> idsPlatformDetails= new ArrayList<Integer>();
                for (Platform p :
                        platforms) {
                    idsPlatforms.add(p.getId());
                }
                for (PlatformDetail pd :
                        platformDetails) {
                    idsPlatformDetails.add(pd.getId());
                }
                Call<GameXML> callToGame;
                totPlatform = new Integer(tempPlatformList.size());
                for (int i = 0; i < tempPlatformList.size(); i++) {
                    platformList.add(tempPlatformList.get(i));
                    if (idsPlatforms.contains(tempPlatformList.get(i).getId())&&idsPlatformDetails.contains(tempPlatformList.get(i).getId()))
                        continue;
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


                            synchronized (numReceivedPlatformGame) {
                                numReceivedPlatformGame = numReceivedPlatformGame + 1;
                                checkFinished(platformList);
                            }

                        }

                        @Override
                        public void onFailure(Call<GameXML> call, Throwable t) {
                            synchronized (numReceivedPlatformGame) {
                                numReceivedPlatformGame = numReceivedPlatformGame + 1;
                                checkFinished(platformList);
                            }
                        }
                    });
                    Data.getData().getCallToGame().add(callToGame);

                    Call<PlatformDetailXML> callToPlatformDetail = possibleAPI.getPlatformDetail(platformList.get(i).getId());
                    callToPlatformDetail.enqueue(new Callback<PlatformDetailXML>() {
                        @Override
                        public void onResponse(Call<PlatformDetailXML> call, Response<PlatformDetailXML> response) {
                            Filter filter = new Filter();
                            synchronized (platformList) {
                                filter.addDetailsToPlatform(platformList, response.body().getPlatformDetail());
                            }
                            synchronized (numReceivedPlatformDetail) {
                                numReceivedPlatformDetail = numReceivedPlatformDetail + 1;
                                checkFinished(platformList);
                            }
                        }

                        @Override
                        public void onFailure(Call<PlatformDetailXML> call, Throwable t) {
                            synchronized (numReceivedPlatformDetail) {
                                numReceivedPlatformDetail = numReceivedPlatformDetail + 1;
                                checkFinished(platformList);
                            }
                        }
                    });

                }




            }

            @Override
            public void onFailure(Call<PlatformXML> call, Throwable t) {
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
                if(numPlatformInDatabase==0)
                    System.out.println("IL NUMERO DI PIATTAFORME E' INIZIALMENTE 0");
                    notifyObserver();
            }
        }
    }

    public void setObserver(SampleFragmentPagerAdapter observer) {
        this.observer = observer;
    }

    public void notifyObserver(){
        if (this.observer!=null)
            this.observer.notifyDataSetChanged();
    }

    public void setNumPlatformInDatabase(int numPlatformInDatabase) {
        this.numPlatformInDatabase = numPlatformInDatabase;
    }

    public class MyAsyncTask extends AsyncTask<List<Platform>, Void, Bitmap> {
        private int MAX = 5;
        private SampleFragmentPagerAdapter observer;
        private String developName=null;
        @Override
        protected Bitmap doInBackground(final List<Platform>... platformOfSpecifiedDeveloper) {
            boolean atLeastOne=false;
            List<GameDetail> gameDetails = SQLite.select().from(GameDetail.class).queryList();
            List<Integer> ids= new ArrayList<Integer>();
            for (GameDetail gd :
                    gameDetails) {
                ids.add(gd.getId());
            }
            int gameId;
            Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
            Call<GameDetailXML> callToGameDetail;
            for (int i = 0; i < platformOfSpecifiedDeveloper[0].size(); i++) {
                for (int j = 0; j < platformOfSpecifiedDeveloper[0].get(i).getGameList().size() && j < MAX; j++) {
                    gameId = platformOfSpecifiedDeveloper[0].get(i).getGameList().get(j).getId();
                    if (ids.contains(gameId)){
                        continue;
                    }
                    //System.out.println("GIOCHI PIATTAFORMA: "+platformOfSpecifiedDeveloper[0].get(i).getId()+platformOfSpecifiedDeveloper[0].get(i).getGameList());
                    callToGameDetail = possibleAPI.getGameDetail(gameId);
                    final int finalI = i;
                    final int finalJ = j;
                    callToGameDetail.enqueue(new Callback<GameDetailXML>() {
                        @Override
                        public void onResponse(Call<GameDetailXML> call, Response<GameDetailXML> response) {
                            GameDetail gameDetail = response.body().gameDetail;
                            //IMPOSTO ULTERIORI DATI PER IL DB E SALVO LE IMMAGINI CON FANART E BOX NEL DB
                            if (gameDetail != null) {
                                System.out.println("RICEVO DETTAGLI: " + gameDetail.getGameTitle());
                                gameDetail.getImages().setIdGame(gameDetail.getId());
                                gameDetail.getImages().save();
                                FlowManager.getModelAdapter(GameDetail.class).save(gameDetail);
                                if ((finalI + 1) >= platformOfSpecifiedDeveloper[0].size() &&
                                        ((finalJ + 1) >= platformOfSpecifiedDeveloper[0].get(finalI).getGameList().size() || (finalJ + 1) >= MAX)) {
                                    System.out.println("NOTIFICO RICEZIONE DI TUTTI I DETTAGLI");
                                    if(developName!=null){
                                        if (!developName.isEmpty()){
                                            notifyObserver(developName);
                                        }
                                    }else
                                        notifyObserver();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GameDetailXML> call, Throwable t) {

                        }

                    });
                    Data.getData().getCallToGameDetail().add(callToGameDetail);
                }
            }
            return null;
        }

        private void notifyObserver(String developName) {
            if (this.observer!=null){
                this.observer.notifyDataSetChanged(developName);
                System.out.println("SONO IL TASK: NOTIFICO L'OBSERVER "+ developName);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            System.out.println("POST EXECUTE");
        }

        public void setObserver(SampleFragmentPagerAdapter observer){
            this.observer=observer;
        }
        private void notifyObserver(){
            if (this.observer!=null){
                this.observer.notifyDataSetChanged();
            System.out.println("SONO IL TASK: NOTIFICO L'OBSERVER");
            }
        }

        public void setDevelopName(String developName) {
            this.developName = developName;
        }
        public void setMAX(int nMax){
            MAX=nMax;
        }
    }

    public class TaskLoadMore extends AsyncTask<Platform, Void, Bitmap> {
        private int MAX = 30;
        private HorizontalAdapter observer;
        private String platformName=null;

        @Override
        protected Bitmap doInBackground(final Platform... platform) {
            boolean atLeastOne=false;
            List<GameDetail> gameDetails = SQLite.select().from(GameDetail.class).queryList();
            List<Integer> ids= new ArrayList<Integer>();
            for (GameDetail gd :
                    gameDetails) {
                ids.add(gd.getId());
            }
            int gameId;
            Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            final PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
            Call<GameDetailXML> callToGameDetail;
                for (int j = 0; j < platform[0].getGameList().size() && j < MAX; j++) {
                    gameId = platform[0].getGameList().get(j).getId();
                    if (ids.contains(gameId)){
                        continue;
                    }
                    //System.out.println("GIOCHI PIATTAFORMA: "+platformOfSpecifiedDeveloper[0].get(i).getId()+platformOfSpecifiedDeveloper[0].get(i).getGameList());
                    callToGameDetail = possibleAPI.getGameDetail(gameId);
                    final int finalJ = j;
                    final List<Integer> idsNewGameDetail= new ArrayList<Integer>();
                    callToGameDetail.enqueue(new Callback<GameDetailXML>() {
                        @Override
                        public void onResponse(Call<GameDetailXML> call, Response<GameDetailXML> response) {
                            GameDetail gameDetail = response.body().gameDetail;
                            //IMPOSTO ULTERIORI DATI PER IL DB E SALVO LE IMMAGINI CON FANART E BOX NEL DB
                            if (gameDetail != null) {
                                System.out.println("RICEVO DETTAGLI: " + gameDetail.getGameTitle());
                                gameDetail.getImages().setIdGame(gameDetail.getId());
                                gameDetail.getImages().save();
                                FlowManager.getModelAdapter(GameDetail.class).save(gameDetail);
                                idsNewGameDetail.add(gameDetail.getId());
                                    System.out.println("NOTIFICO RICEZIONE DI TUTTI I DETTAGLI");
                                    if(platformName!=null){
                                        if (!platformName.isEmpty()){
                                            //DEVO NOTIFICARE L'ADAPTER
                                            System.out.println("LOAD MORE NOTIFICA Horizontal Adapter ");
                                            notifyObserver(idsNewGameDetail);
                                        }
                                    }
                            }
                        }

                        @Override
                        public void onFailure(Call<GameDetailXML> call, Throwable t) {
                        }

                    });
                    Data.getData().getCallToGameDetail().add(callToGameDetail);
                }

            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            System.out.println("POST EXECUTE");
        }

        public void setObserver(HorizontalAdapter observer){
            this.observer=observer;
        }

        private void notifyObserver(List<Integer> idsNewGameDetail){
            if (this.observer!=null){
                this.observer.addNewIds(idsNewGameDetail);
                System.out.println("SONO IL LOADMORETASK: NOTIFICO L'Horizontal Adapter - "+platformName);
            }
        }

        public void setPlatformName(String platformName){
            this.platformName=platformName;
        }
    }

}

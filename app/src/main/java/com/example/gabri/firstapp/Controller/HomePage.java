package com.example.gabri.firstapp.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Adapter.CoverFlowAdapter;
import com.example.gabri.firstapp.Adapter.GameAdapter;
import com.example.gabri.firstapp.GameEntity;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.Game;
import com.example.gabri.firstapp.Model.GameCover;
import com.example.gabri.firstapp.Model.Platform;
import com.example.gabri.firstapp.PlatformXML;
import com.example.gabri.firstapp.R;
import com.example.gabri.firstapp.PlatformRequestAPI;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;


import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class HomePage extends ActionBarActivity {

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mAdapter;
    private ArrayList<GameEntity> mData = new ArrayList<>(0);
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GameCover> gameCoverList;
    private RecyclerView recyclerViewTwo;
    private RecyclerView.Adapter adapterTwo;
    private List<GameCover> gameCoverListTwo;
    public static final String BASE_URL = "http://thegamesdb.net/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);





        mData.add(new GameEntity(R.drawable.image_1, R.string.title1));
        mData.add(new GameEntity(R.drawable.image_2, R.string.title2));
        mData.add(new GameEntity(R.drawable.image_3, R.string.title3));
        mData.add(new GameEntity(R.drawable.image_4, R.string.title4));
        mData.add(new GameEntity(R.drawable.image_5, R.string.title4));
        mData.add(new GameEntity(R.drawable.image_6, R.string.title4));



        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);


        mAdapter = new CoverFlowAdapter(this);
        mAdapter.setData(mData);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlow.setAdapter(mAdapter);



        recyclerView= (RecyclerView) findViewById((R.id.toptenrecycleview));
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(LayoutManager);
        gameCoverList=new ArrayList<>();

        for(int i=0; i<200;i++){
            GameCover temp= new GameCover(null,"wooooo");
            gameCoverList.add(temp);
        }

        adapter=new GameAdapter(gameCoverList,this);
        recyclerView.setAdapter(adapter);


        recyclerViewTwo= (RecyclerView) findViewById((R.id.mostdownloadedrecycleview));
        recyclerViewTwo.setHasFixedSize(true);
        LinearLayoutManager LayoutManagerTwo = new LinearLayoutManager(this);
        LayoutManagerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTwo.setLayoutManager(LayoutManagerTwo);
        gameCoverListTwo=new ArrayList<>();

        for(int i=0; i<100;i++){
            GameCover temp= new GameCover(null,"wooooo");
            gameCoverListTwo.add(temp);
        }

        adapterTwo=new GameAdapter(gameCoverListTwo,this);
        recyclerViewTwo.setAdapter(adapterTwo);


    getPlatformList();
    }

    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coverflow_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPlatformList() {
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
                final List<List<Game>> gameListForEachPlatform = new ArrayList<List<Game>>();
                Call<GameXML> callToGame;
                for(int i=0;i<platformList.size();i++){
                    System.out.println(platformList.get(i).getName());
                    callToGame=possibleAPI.getGame(platformList.get(i).getName());
                    callToGame.enqueue(new Callback<GameXML>() {
                        @Override
                        public void onResponse(Call<GameXML> call, Response<GameXML> response) {
                            List<Game> gameList=response.body().getGameList();
                            System.out.println("Lista ricevuta dimensione: "+gameList.size());
                            filter.getNewestGame(gameList);
                            gameListForEachPlatform.add(gameList);
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
}

package com.example.gabri.firstapp;

import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Model.Game;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.Callback;


/**
 * Created by Gabri on 12/11/17.
 */

public class GameRequestAPI implements Callback<GameXML> {

    public static final String BASE_URL = "http://thegamesdb.net/api/";


    public void startGameRequest(String platformName) {

        Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
        Call<GameXML> call = possibleAPI.getGame(platformName);
        call.enqueue(this);

    }


    @Override
    public void onResponse(Call<GameXML> call, Response<GameXML> response) {
        if(response.isSuccessful()){
            String platformName;
            GameXML game=response.body();

        }
        else
            System.out.println("ciaooo");

    }


    @Override
    public void onFailure(Call<GameXML> call, Throwable t) {
        System.out.println(t.getCause().getMessage());
        System.out.println(t.getMessage());

        System.out.println("error");
    }
}


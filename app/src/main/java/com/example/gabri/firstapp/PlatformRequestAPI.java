package com.example.gabri.firstapp;

import com.example.gabri.firstapp.API.PossibleAPI;
import com.example.gabri.firstapp.Model.Platform;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.Callback;


/**
 * Created by Gabri on 12/11/17.
 */

public class PlatformRequestAPI implements Callback<PlatformXML> {

    public static final String BASE_URL = "http://thegamesdb.net/api/";
    public List<Platform> platformList;


    public void startPlatformRequest() {

    Retrofit retrofitObject = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    PossibleAPI possibleAPI = retrofitObject.create(PossibleAPI.class);
    Call<PlatformXML> call = possibleAPI.getPlatform();
    call.enqueue(this);

    }


    @Override
    public void onResponse(Call<PlatformXML> call, Response<PlatformXML> response) {
        if(response.isSuccessful()){
            String platformName;
            PlatformXML platforms=response.body();
            setPlatformList(platforms.getPlatformList());
            }
        else
            System.out.println("ciaooo");

    }


    @Override
    public void onFailure(Call<PlatformXML> call, Throwable t) {
        System.out.println(t.getCause().getMessage());
        System.out.println(t.getMessage());

        System.out.println("error");
    }

    public void setPlatformList(List<Platform> platform){
        this.platformList=platform;
    }

    public List<Platform> getPlatformList(){
       return this.platformList;
    }
}


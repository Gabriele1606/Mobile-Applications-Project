package com.example.gabri.firstapp.API;


import com.example.gabri.firstapp.GameDetailXML;
import com.example.gabri.firstapp.GameXML;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.PlatformDetail;
import com.example.gabri.firstapp.PlatformDetailXML;
import com.example.gabri.firstapp.PlatformXML;
import com.example.gabri.firstapp.RSSList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gabri on 12/11/17.
 */

public interface PossibleAPI {
    @GET("GetGamesList.php")
    Call<GameXML> getGame(@Query("platform") String console);

    @GET("GetPlatformsList.php")
    Call<PlatformXML> getPlatform();

    @GET("/feed/rss/news/")
    Call<RSSList> getRssList();

    @GET("GetPlatform.php")
    Call<PlatformDetailXML> getPlatformDetail(@Query("id") int id);

    @GET("GetGame.php")
    Call<GameDetailXML> getGameDetail(@Query("id") int id);
}
